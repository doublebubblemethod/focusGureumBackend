package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import com.focusGureumWebApp.focusGureumWebdemo.repository.CategoryRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void createTask(String name, boolean status, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Task task = new Task();
        task.setName(name);
        task.setStatus(status);
        task.setCategory(category);
        taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
    public void deleteTasksByCategoryId(Integer CategoryId) {
        taskRepository.deleteByCategory_Id(CategoryId);
    }
    public void toggleTask(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(!task.isStatus());
        taskRepository.save(task);
    }

    public void updateTaskName(Integer id, String newName) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setName(newName);
        taskRepository.save(task);
    }
}
