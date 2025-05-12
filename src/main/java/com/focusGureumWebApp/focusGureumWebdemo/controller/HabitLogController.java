package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/habitLogs")
public class HabitLogController {
    private final HabitLogService habitLogService;
    public HabitLogController(HabitLogService habitLogService) {
        this.habitLogService = habitLogService;
    }

    @GetMapping("/{habitId}")
    public String getHabitLogService(@PathVariable Integer habitId, Model model) {
        List<HabitLog> habitLogs = habitLogService.getAllByHabit_Id(habitId);
        model.addAttribute("habitLogs", habitLogs);
        return "habitLogs";
    }
}
