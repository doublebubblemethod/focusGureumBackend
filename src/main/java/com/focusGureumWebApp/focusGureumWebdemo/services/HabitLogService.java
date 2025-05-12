package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitLogService {
    private final HabitLogRepository habitLogRepository;

    public HabitLogService(HabitLogRepository habitLogRepository) {
        this.habitLogRepository = habitLogRepository;
    }

    public List<HabitLog> getAllByHabit_Id(Integer habitId) {
        return habitLogRepository.findAllByHabit_Id(habitId);
    }
}
