package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.dto.TaskRequest;
import com.focusGureumWebApp.focusGureumWebdemo.dto.TaskResponse;
import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import com.focusGureumWebApp.focusGureumWebdemo.services.CategoryService;
import com.focusGureumWebApp.focusGureumWebdemo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    public TaskService(TaskRepository taskRepository, CategoryService categoryService) {
        this.taskRepository = taskRepository;
        this.categoryService = categoryService;
    }

    public List<TaskResponse> getTasksByCategory(String nickname, Integer categoryId) {
        try {
            // Validate user has access to this category, throws if not authorized
            Category category = categoryService.getCategoryForUser(categoryId, nickname);
            List<Task> tasks = taskRepository.findAllByCategory(category);

            return tasks.stream()
                    .map(task -> new TaskResponse(
                            task.getName(),
                            task.isStatus(),
                            task.getCategory().getId(),
                            task.getId()
                    ))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }


    public Task createTask(String nickname, TaskRequest request) {
        Category category = categoryService.getCategoryForUser(request.getCategoryId(), nickname);
        Task task = new Task();
        task.setName(request.getName());
        task.setStatus(request.isStatus());
        task.setCategory(category);

        return taskRepository.save(task);
    }

    public void deleteTask(Integer taskId, Integer categoryId, String nickname) {
        Category category = categoryService.getCategoryForUser(categoryId, nickname);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getCategory().getId().equals(category.getId())) {
            throw new RuntimeException("Task does not belong to the specified category");
        }
        taskRepository.delete(task);
    }


    public void toggleTaskStatus(Integer taskId, Integer categoryId, String nickname) {
            Category category = categoryService.getCategoryForUser(categoryId, nickname);
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            if (!task.getCategory().getId().equals(category.getId())) {
                throw new RuntimeException("Task does not belong to the specified category");
            }
            task.setStatus(!task.isStatus());
            taskRepository.save(task);
    }


    public void updateTaskName(String nickname, Integer categoryId, Integer taskId, String newName) {
        Category category = categoryService.getCategoryForUser(categoryId, nickname);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getCategory().getId().equals(category.getId())) {
            throw new RuntimeException("Task does not belong to the specified category");
        }
        task.setName(newName);
        taskRepository.save(task);
    }
}
