package com.focusGureumWebApp.focusGureumWebdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitLogResponse {
    private Integer id;
    private LocalDate date;
    private Integer habitId;  // only id, no full habit object
}
