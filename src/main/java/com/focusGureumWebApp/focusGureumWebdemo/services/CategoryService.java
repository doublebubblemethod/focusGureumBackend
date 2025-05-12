package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllByUser_Id(Integer userId) {
        return categoryRepository.findAllByUser_Id(userId);
    }
}
