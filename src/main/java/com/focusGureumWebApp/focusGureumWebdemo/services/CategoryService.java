package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.repository.AppUserRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;
    
    public CategoryService(CategoryRepository categoryRepository, AppUserRepository appUserRepository) {
        this.categoryRepository = categoryRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<Category> getAllByUser_Id(Integer userId) {
        return categoryRepository.findAllByUser_Id(userId);
    }
    public void createCategory(String name, boolean status, String imagePath, Integer userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = new Category();
        category.setName(name);
        category.setStatus(status);
        category.setImagePath(imagePath);
        category.setUser(user);
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public void toggleCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setStatus(!category.isStatus());
        categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
