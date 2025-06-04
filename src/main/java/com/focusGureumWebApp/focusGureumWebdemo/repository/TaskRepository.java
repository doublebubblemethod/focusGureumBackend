package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Transactional
    void deleteByCategory_Id(Integer categoryId);
}
