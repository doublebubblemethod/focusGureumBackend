package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitResponse;
import com.focusGureumWebApp.focusGureumWebdemo.dto.HabitScheduleCalendarResponse;
import com.focusGureumWebApp.focusGureumWebdemo.dto.ScheduleResponse;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitScheduleService;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/habitschedule")
public class HabitScheduleController {
    private final HabitScheduleService scheduleService;
    private final HabitService habitService;
    public HabitScheduleController(HabitScheduleService scheduleService, HabitService habitService) {
        this.scheduleService = scheduleService;
        this.habitService = habitService;
    }

    // 1. Get schedule
    @GetMapping()
    public ResponseEntity<?> getSchedule(Authentication authentication) {
        try {
            String nickname = authentication.getName();
            List<ScheduleResponse> schedule = scheduleService.getAllSchedulesByUser(nickname);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Schedule not found for habit\n" + e.getMessage());
        }
    }
    /*
    Function calculates habits a user has to do today; example output:
        [
        "Morning Jog",
        "Drink 8 Glasses of Water",
        "Daily Stretching",
        "Track Meals in App"
        ]
     */
    @GetMapping("/today")
    public ResponseEntity<?> areHabitDueToday(Authentication authentication) {
        try {
            String nickname = authentication.getName();
            List<HabitResponse> allHabits = habitService.findActiveHabitsByNickname(nickname);
            List<String> scheduledHabitNames = new ArrayList<>();
            for (HabitResponse habit : allHabits) {
                if(scheduleService.isHabitScheduledOnDate(habit, LocalDate.now())) {
                    scheduledHabitNames.add(habit.getName());
                }
            }
            return ResponseEntity.ok(scheduledHabitNames);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Failed to get schedule\n" + e.getMessage());
        }
    }
    //List<LocalDate> scheduledThisMonth = getScheduledDatesInRange(habit, YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth());
    @GetMapping("/monthplan")
    public ResponseEntity<?> HabitsPlannedThisMonth(Authentication authentication) {
        try {
            String nickname = authentication.getName();
            List<HabitResponse> allHabits = habitService.findActiveHabitsByNickname(nickname);
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            List<HabitScheduleCalendarResponse> calendarResponses = allHabits.stream()
                    .map(habit -> new HabitScheduleCalendarResponse(
                            habit.getId(),
                            habit.getName(),
                            scheduleService.getScheduledDatesInRange(habit, startOfMonth, endOfMonth)
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(calendarResponses);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Failed to get month schedule\n" + e.getMessage());
        }
    }

    @PatchMapping("/frequency-type/{habitId}")
    public ResponseEntity<?> updateFrequencyType(Authentication authentication, @PathVariable Integer habitId,
                                                 @RequestBody Map<String, String> body) {
        try {
            String nickname = authentication.getName();
            String newType = body.get("frequencyType");
            String daysOfWeek = body.get("daysOfWeek"); // optional
            String dayStepStr = body.get("dayStep");    // optional

            scheduleService.updateFrequencyType(nickname, habitId, newType, daysOfWeek, dayStepStr);
            return ResponseEntity.ok("Frequency type updated and synced");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
