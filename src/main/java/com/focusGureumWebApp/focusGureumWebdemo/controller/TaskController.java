package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import com.focusGureumWebApp.focusGureumWebdemo.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping
    public String getTaskService(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks";
    }
    @PostMapping
    public String createTask(@RequestParam String name, @RequestParam boolean status, @RequestParam Integer category_id) {
        taskService.createTask(name, status, category_id);
        return "redirect:/tasks";
    }
    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/toggle")
    public String toggleTask(@PathVariable Integer id) {
        taskService.toggleTask(id);
        return "redirect:/tasks";
    }
}
