package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitLogResponse;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitLogRepository;
import io.micrometer.common.KeyValues;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitLogService {
    private final HabitLogRepository habitLogRepository;
    private final HabitService habitService;
    public HabitLogService(HabitLogRepository habitLogRepository, HabitService habitService) {
        this.habitLogRepository = habitLogRepository;
        this.habitService = habitService;
    }


    public List<HabitLogResponse> findAllLogs(String nickname, Integer habitId) {
        try {
            Habit habit = habitService.getHabitForUser(habitId, nickname);
            List<HabitLog> logs = habitLogRepository.findAllByHabit(habit);

            return logs.stream()
                    .map(log -> new HabitLogResponse(
                            log.getId(),
                            log.getDate(),
                            log.getHabit().getId()
                    ))
                    .collect(Collectors.toList());
        }catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    public List<HabitLogResponse> findLogsByDateRange(String nickname, Integer habitId, LocalDate startDate, LocalDate endDate) {
        try {
            Habit habit = habitService.getHabitForUser(habitId, nickname);
            List<HabitLog> logs;
            if (startDate != null && endDate != null) {
                logs = habitLogRepository.findByHabitAndDateBetween(habit, startDate, endDate);
            } else if (startDate != null) {
              logs = habitLogRepository.findByHabitAndDate(habit, startDate);
            } else {
                // If no dates provided, return all logs for habit
                logs = habitLogRepository.findAllByHabit(habit);
            }
            return logs.stream()
                    .map(log -> new HabitLogResponse(
                            log.getId(),
                            log.getDate(),
                            log.getHabit().getId()
                    ))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            // log if needed
            return Collections.emptyList();
        }
    }

    // Create a new HabitLog for a user's habit on a specific date
    public HabitLog createHabitLog(String nickname, Integer habitId, LocalDate date) {
        // Check if habit belongs to user
        Habit habit = habitService.getHabitForUser(habitId, nickname);

        // Create new log
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setDate(date);

        return habitLogRepository.save(habitLog);
    }

    // Delete a habit log by ID (only if belongs to user's habit)
    public void deleteHabitLog(String nickname, Integer logId) {
        HabitLog log = habitLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("HabitLog not found"));

        // Verify habit belongs to user
        if (!log.getHabit().getUser().getNickname().equals(nickname)) {
            throw new AccessDeniedException("User not authorized to delete this log");
        }

        habitLogRepository.delete(log);
    }

}
