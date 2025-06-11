package com.focusGureumWebApp.focusGureumWebdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitScheduleCalendarResponse {
    private Integer habitId;
    private String habitName;
    private List<LocalDate> scheduledDates;
}
