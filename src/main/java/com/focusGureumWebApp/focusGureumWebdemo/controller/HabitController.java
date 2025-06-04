package com.focusGureumWebApp.focusGureumWebdemo.controller;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @PatchMapping("/{habitId}")
    public ResponseEntity<Habit> renameHabit(@PathVariable Integer userId, @PathVariable Integer habitId, @RequestBody Map<String, String> update) {
        String newName = update.get("name");
        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Habit updatedHabit = habitService.renameHabit(habitId, newName);
            return ResponseEntity.ok(updatedHabit);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
        }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleHabit(@PathVariable Integer userId, @PathVariable Integer habitId) {
        try {
            habitService.toggleHabit(habitId);
            return ResponseEntity.ok("Habit status toggled successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling Habit status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Integer userId, @PathVariable Integer habitId) {
        try {
            habitService.deleteHabit(habitId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}