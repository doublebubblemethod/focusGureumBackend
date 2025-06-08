package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Integer> {
    List<HabitLog> findAllByHabit(Habit habit);
    List<HabitLog> findByHabitAndDate(Habit habit, LocalDate date);
    List<HabitLog> findByHabitAndDateBetween(Habit habit, LocalDate startDate, LocalDate endDate);
}
