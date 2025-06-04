package com.focusGureumWebApp.focusGureumWebdemo.services;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.HabitScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
    public List<Habit> getHabitsByUserId(Integer userId) {
        return habitRepository.findByUserId(userId);
    }
    public Habit getById(Integer id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
    }
    public void deleteHabit(Integer habitId) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new RuntimeException("Habit not found"));
        //delete related habit schedule and logs
        habitRepository.delete(habit);
    }
    public Habit renameHabit(Integer habitId, String newName) {
        // Find the habit by ID
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new RuntimeException("Habit not found"));
        habit.setName(newName);
        return habitRepository.save(habit);
    }
    public void toggleHabit(Integer id) {
        Habit habit = habitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        habit.setActive(!habit.isActive());
        habitRepository.save(habit);
    }
}
