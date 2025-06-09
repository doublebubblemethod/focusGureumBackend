package com.focusGureumWebApp.focusGureumWebdemo.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String name;
    private boolean status;
    private Integer categoryId;
    private Integer id;
}
