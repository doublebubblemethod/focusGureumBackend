package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.HabitSchedule;
import com.focusGureumWebApp.focusGureumWebdemo.services.HabitScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/habitSchedules")
public class HabitSceduleController {
    private final HabitScheduleService habitScheduleService;
    public HabitSceduleController(HabitScheduleService habitScheduleService) {
        this.habitScheduleService = habitScheduleService;
    }

    @GetMapping("/{habitId}")
    public String getHabitScheduleService(@PathVariable Integer habitId, Model model) {
        List<HabitSchedule> habitSchedules = habitScheduleService.getAllByHabit_Id(habitId);
        model.addAttribute("habitSchedules", habitSchedules);
        return "habitSchedules";
    }
}
