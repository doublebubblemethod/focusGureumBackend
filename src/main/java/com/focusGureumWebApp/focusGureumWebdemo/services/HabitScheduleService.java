package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitResponse;
import com.focusGureumWebApp.focusGureumWebdemo.dto.ScheduleResponse;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class HabitScheduleService {
    private final HabitScheduleRepository habitScheduleRepository;
    private final HabitService habitService;

    public HabitScheduleService(HabitScheduleRepository habitScheduleRepository, HabitService habitService) {
        this.habitScheduleRepository = habitScheduleRepository;
        this.habitService = habitService;
    }

    public List<ScheduleResponse> getAllSchedulesByUser(String nickname) {
        List<HabitResponse> habitResponses = habitService.findActiveHabitsByNickname(nickname);
        List<Integer> habitIds = habitResponses.stream()
                .map(HabitResponse::getId)
                .collect(Collectors.toList());
        List<HabitSchedule> schedules = habitScheduleRepository.findByHabit_IdIn(habitIds);

        if (schedules == null || schedules.isEmpty()) {
            throw new RuntimeException("No schedules found for user: " + nickname);
        }

        return schedules.stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getId(),
                        schedule.getFrequencyType(),
                        schedule.getDayStep(),
                        schedule.getDaysOfWeek(),
                        schedule.getHabit().getId()
                ))
                .collect(Collectors.toList());
    }

    public void updateFrequencyType(String nickname, Integer habitId,
                                    String frequencyType,
                                    String daysOfWeek,
                                    String dayStepStr) throws IllegalAccessException {

        HabitSchedule schedule = habitScheduleRepository.findByHabit_Id(habitId);
        if (schedule == null) {
            throw new RuntimeException("Schedule not found");
        }
        if (!schedule.getHabit().getUser().getNickname().equals(nickname)) {
            throw new IllegalAccessException("Unauthorized");
        }
        String type = frequencyType.toLowerCase().trim();
        switch (type) {
            case "step":
                if (dayStepStr == null) {
                    throw new IllegalArgumentException("dayStep is required for 'step' frequency type.");
                }
                int dayStep;
                try {
                    dayStep = Integer.parseInt(dayStepStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("dayStep must be an integer.");
                }

                if (dayStep < 1 || dayStep > 31) {
                    throw new IllegalArgumentException("dayStep must be between 1 and 31.");
                }
                schedule.setFrequencyType("step");
                schedule.setDayStep(dayStep);
                schedule.setDaysOfWeek(null); // clear opposite
                break;

            case "week":
                if (daysOfWeek == null || !daysOfWeek.matches("^[1-7](,[1-7])*$")) {
                    throw new IllegalArgumentException("daysOfWeek must be a comma-separated list of digits 1–7 (e.g., '1,3,5').");
                }

                schedule.setFrequencyType("week");
                schedule.setDaysOfWeek(daysOfWeek);
                schedule.setDayStep(null); // clear opposite
                break;

            default:
                throw new IllegalArgumentException("frequencyType must be 'week' or 'step'.");
        }

        habitScheduleRepository.save(schedule);
    }

    public boolean isHabitScheduledOnDate(HabitResponse habit, LocalDate date) {
        HabitSchedule schedule = habitScheduleRepository.findByHabit_Id(habit.getId());
        if (schedule == null) return false;

        String frequencyType = schedule.getFrequencyType();
        if (frequencyType == null) return false;

        switch (frequencyType.toLowerCase()) {
            case "step" -> {
                if (schedule.getDayStep() == null) return false;
                long daysSinceStart = ChronoUnit.DAYS.between(habit.getCreatedAt().toLocalDate(), date);
                return daysSinceStart >= 0 && daysSinceStart % schedule.getDayStep() == 0;
            }

            case "week" -> {
                if (schedule.getDaysOfWeek() == null || schedule.getDaysOfWeek().isBlank()) return false;
                int targetDay = date.getDayOfWeek().getValue(); // 1 = Monday, ..., 7 = Sunday
                Set<Integer> scheduledDays = Arrays.stream(schedule.getDaysOfWeek().split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                return scheduledDays.contains(targetDay);
            }

            default -> {
                return false; // Unknown frequency type
            }
        }
    }


    public List<LocalDate> getScheduledDatesInRange(HabitResponse habit, LocalDate start, LocalDate end) {
        List<LocalDate> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (isHabitScheduledOnDate(habit, date)) {
                result.add(date);
            }
        }
        return result;
    }
}
