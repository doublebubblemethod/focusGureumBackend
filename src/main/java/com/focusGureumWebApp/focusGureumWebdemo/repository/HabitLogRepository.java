package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Integer> {
    List<HabitLog> findAllByHabit_Id(Integer habitId);
}
