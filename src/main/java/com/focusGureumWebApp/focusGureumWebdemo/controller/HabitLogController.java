package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitLogResponse;
import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/habitlogs")
public class HabitLogController {
    private final HabitLogService habitLogService;
    public HabitLogController(HabitLogService habitLogService) {
        this.habitLogService = habitLogService;
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<List<HabitLogResponse>> getHabitLogService(Authentication authentication, @PathVariable Integer habitId) {
        String nickname = authentication.getName();
        List<HabitLogResponse> logs = habitLogService.findAllLogs(nickname, habitId);
        return ResponseEntity.ok(logs);
    }
    /*
    get all logs counts. example:
        curl --location 'http://localhost:8080/api/habitlogs/monthly-counts?year=2025&month=03' \
        --header 'Authorization: ••••••' \
        --data ''
     */
    @GetMapping("/monthly-counts")
    public ResponseEntity<Map<LocalDate, Integer>> getMonthlyHabitLogCounts(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            Authentication authentication) {
        String nickname = authentication.getName();
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.of(year, month);
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().build();
        }
        Map<LocalDate, Integer> counts = habitLogService.getCheckedHabitCountsByDate(nickname, yearMonth);
        return ResponseEntity.ok(counts);
    }
    @PostMapping("/{habitId}/addlog")
    public ResponseEntity<?> createHabitLog(
            Authentication authentication,
            @PathVariable Integer habitId,
            @RequestBody Map<String, String> body
    ) {
        try {
            String nickname = authentication.getName();
            String dateStr = body.get("date");

            if (dateStr == null) {
                return ResponseEntity.badRequest().body("Date is required");
            }
            LocalDate date = LocalDate.parse(dateStr);
            HabitLog createdLog = habitLogService.createHabitLog(nickname, habitId, date);
            return ResponseEntity.ok("new log was created with id " + createdLog.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating habit log: " + e.getMessage());
        }
    }
    // Delete a habit log by ID
    @DeleteMapping("/{logId}/delete")
    public ResponseEntity<?> deleteHabitLog(
            Authentication authentication,
            @PathVariable Integer logId
    ) {
        try {
            String nickname = authentication.getName();
            habitLogService.deleteHabitLog(nickname, logId);
            return ResponseEntity.ok("Habit log deleted successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
