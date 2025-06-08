package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Integer> {
    List<Habit> findByUserNickname(String userNickname);

}