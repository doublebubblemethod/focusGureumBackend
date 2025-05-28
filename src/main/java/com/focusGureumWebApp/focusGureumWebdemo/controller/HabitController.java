package com.focusGureumWebApp.focusGureumWebdemo.controller;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getHabitsByUserId(@PathVariable Integer userId) {
        List<Habit> habits = habitService.getHabitsByUserId(userId);
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<Habit> getHabitById(
            @PathVariable Integer userId,
            @PathVariable Integer habitId
    ) {
        try {
            Habit habit = habitService.getById(habitId);
            // Check if this habit belongs to the given user
            if (!habit.getUser().getId().equals(userId)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(habit); // resolve ambiguity
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}