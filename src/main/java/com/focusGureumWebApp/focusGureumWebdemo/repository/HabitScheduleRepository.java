package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitScheduleRepository extends JpaRepository<HabitSchedule, Integer> {
    List<HabitSchedule> findAllByHabit_Id(Integer habitId);
}
