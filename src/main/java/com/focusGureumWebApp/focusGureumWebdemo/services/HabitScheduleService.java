package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitScheduleService {
    private final HabitScheduleRepository habitScheduleRepository;

    public HabitScheduleService(HabitScheduleRepository habitScheduleRepository) {
        this.habitScheduleRepository = habitScheduleRepository;
    }

    public List<HabitSchedule> getAllByHabit_Id(Integer habitId) {
        return habitScheduleRepository.findAllByHabit_Id(habitId);
    }
}
