package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import com.focusGureumWebApp.focusGureumWebdemo.models.HabitLog;
import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Transactional
    void deleteByCategory_Id(Integer categoryId);

    List<Task> findAllByCategory(Category category);
}
