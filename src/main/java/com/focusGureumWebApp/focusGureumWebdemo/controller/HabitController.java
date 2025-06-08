package com.focusGureumWebApp.focusGureumWebdemo.controller;
import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitRequest;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getHabitsByUserNickname(Authentication authentication) {
        String nickname = authentication.getName();
        List<Habit> habits = habitService.findByUserNickname(nickname);
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<Habit> getHabitById(
            Authentication authentication,
            @PathVariable Integer habitId
    ) {
        try {
            String nickname = authentication.getName();
            Habit habit = habitService.getHabitForUser(habitId, nickname);
            return ResponseEntity.ok(habit);
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{habitId}/rename")
    public ResponseEntity<Habit> renameHabit(Authentication authentication, @PathVariable Integer habitId, @RequestBody Map<String, String> update) {
        try {
            String nickname = authentication.getName();
            Habit habit = habitService.getHabitForUser(habitId, nickname);
            String newName = update.get("name");
            if (newName == null || newName.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Habit updatedHabit = habitService.renameHabit(habit, newName);
            return ResponseEntity.ok(updatedHabit);
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{habitId}/toggle")
    public ResponseEntity<?> toggleHabit(Authentication authentication, @PathVariable Integer habitId) {
        try {
            String nickname = authentication.getName();
            Habit habit = habitService.getHabitForUser(habitId, nickname);
            habitService.toggleHabit(habit);
            return ResponseEntity.ok("Habit status toggled successfully");
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{habitId}/delete")
    public ResponseEntity<?> deleteHabit(Authentication authentication, @PathVariable Integer habitId) {
        try {
            String nickname = authentication.getName();
            Habit habit = habitService.getHabitForUser(habitId, nickname);
            habitService.deleteHabit(habit);
            return ResponseEntity.ok("Habit deleted successfully");
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/create")
    public ResponseEntity<?> createHabit(Authentication authentication, @RequestBody HabitRequest habitRequest) {
        try {
            String nickname = authentication.getName();
            Habit createdHabit = habitService.createHabit(habitRequest, nickname);
            return ResponseEntity.ok(createdHabit);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating habit: " + ex.getMessage());
        }
    }

}