package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HabitScheduleRepository extends JpaRepository<HabitSchedule, Integer> {
    Optional<HabitSchedule> findByHabit_Id(Integer habitId);
}
