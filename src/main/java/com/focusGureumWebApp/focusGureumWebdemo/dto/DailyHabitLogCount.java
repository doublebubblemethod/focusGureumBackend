package com.focusGureumWebApp.focusGureumWebdemo.dto;

import java.time.LocalDate;

public interface DailyHabitLogCount {
    LocalDate getLogDate();
    Integer getHabitCount();
}
