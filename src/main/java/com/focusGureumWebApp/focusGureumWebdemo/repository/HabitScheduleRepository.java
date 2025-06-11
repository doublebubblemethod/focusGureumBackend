package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface HabitScheduleRepository extends JpaRepository<HabitSchedule, Integer> {
    HabitSchedule findByHabit_Id(Integer habitId);
    List<HabitSchedule> findByHabit_IdIn(List<Integer> habitIds);
}
