package com.focusGureumWebApp.focusGureumWebdemo.repository;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByUser_Id(Integer userId);
}

