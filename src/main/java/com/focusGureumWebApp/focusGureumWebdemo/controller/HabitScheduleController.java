package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/{userId}/habits/{habitId}/schedule")
public class HabitScheduleController {
    private final HabitScheduleService scheduleService;

    public HabitScheduleController(HabitScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 1. Get schedule
    @GetMapping
    public ResponseEntity<?> getSchedule(@PathVariable Integer habitId, @PathVariable Integer userId) {
        try {
            HabitSchedule schedule = scheduleService.getByHabitId(userId, habitId);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Schedule not found for habit " + habitId + " and user " + userId + "\n" + e.getMessage());
        }
    }

    // 2. Change frequencyType
//    {
//        "frequencyType": "step",
//            "dayStep": "3"
//    }

    @PatchMapping("/frequency-type")
    public ResponseEntity<?> updateFrequencyType(@PathVariable Integer userId, @PathVariable Integer habitId,
                                                 @RequestBody Map<String, String> body) {
        try {
            String newType = body.get("frequencyType");
            String daysOfWeek = body.get("daysOfWeek"); // optional
            String dayStepStr = body.get("dayStep");    // optional

            scheduleService.updateFrequencyType(userId, habitId, newType, daysOfWeek, dayStepStr);
            return ResponseEntity.ok("Frequency type updated and synced");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
