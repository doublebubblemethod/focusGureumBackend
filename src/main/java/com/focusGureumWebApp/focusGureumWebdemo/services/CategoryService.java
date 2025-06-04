package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.repository.AppUserRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public boolean toggleCategory(Integer id) {
        try {
            // Attempt to find the category by ID
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

            // Toggle the category's status
            category.setStatus(!category.isStatus());

            // Save the updated category
            categoryRepository.save(category);

            // Return true if the operation was successful
            return true;
        } catch (Exception e) {
            // Return false if any error occurs (like category not found, or DB issues)
            return false;
        }
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
    public boolean renameCategory(Integer id, String newName) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return false; // Category not found
        }
        Category category = optionalCategory.get();
        category.setName(newName);
        categoryRepository.save(category);
        return true;
    }

}
