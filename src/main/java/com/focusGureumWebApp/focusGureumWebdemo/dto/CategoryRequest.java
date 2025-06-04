package com.focusGureumWebApp.focusGureumWebdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private boolean status;
    private String imagePath;
    private Integer userId;
}
