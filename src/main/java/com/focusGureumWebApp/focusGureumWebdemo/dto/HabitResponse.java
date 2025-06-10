package com.focusGureumWebApp.focusGureumWebdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitResponse {
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private boolean active;
    private Integer userId;
}
