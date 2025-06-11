package com.focusGureumWebApp.focusGureumWebdemo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Integer id;
    private String frequencyType;
    private Integer dayStep;
    private String daysOfWeek;
    private Integer habitId;
}
