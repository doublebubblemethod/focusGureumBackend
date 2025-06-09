package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.dto.TaskRequest;
import com.focusGureumWebApp.focusGureumWebdemo.dto.TaskResponse;
import com.focusGureumWebApp.focusGureumWebdemo.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getTasksByUserNickname(Authentication authentication, @PathVariable Integer categoryId) {
        try {
            String nickname = authentication.getName();
            List<TaskResponse> tasks = taskService.getTasksByCategory(nickname, categoryId);
            return ResponseEntity.ok(tasks); // 200 OK with the list
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving tasks: " + e.getMessage());
        }
    }
    @PatchMapping("/create")
    public ResponseEntity<String> createTask(Authentication authentication, @RequestBody TaskRequest request) {
        try {
            String nickname = authentication.getName();
            taskService.createTask(nickname, request);
            return ResponseEntity.ok("Task created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{categoryId}/{taskId}/delete")
    public ResponseEntity<?> deleteTask(Authentication authentication,
                                        @PathVariable Integer categoryId,
                                        @PathVariable Integer taskId) {
        try {
            String nickname = authentication.getName();
            taskService.deleteTask(taskId, categoryId, nickname);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting task: " + e.getMessage());
        }
    }

    @PatchMapping("/{categoryId}/{taskId}/toggle")
    public ResponseEntity<?> toggleTask(Authentication authentication, @PathVariable Integer categoryId, @PathVariable Integer taskId) {
        try {
            String nickname = authentication.getName();
            taskService.toggleTaskStatus(taskId, categoryId, nickname);
            return ResponseEntity.ok("Task status toggled successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling task status: " + e.getMessage());
        }
    }

    @PatchMapping("/{categoryId}/{taskId}/rename")
    public ResponseEntity<?> updateTaskName(Authentication authentication, @PathVariable Integer categoryId, @PathVariable Integer taskId,
                                            @RequestBody Map<String, String> body) {
        try {
            String nickname = authentication.getName();
            String newName = body.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Task name cannot be empty");
            }
            taskService.updateTaskName(nickname, categoryId, taskId, newName);
            return ResponseEntity.ok("Task name updated successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating task name: " + e.getMessage());
        }
    }

}
