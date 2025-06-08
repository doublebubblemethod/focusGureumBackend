package com.focusGureumWebApp.focusGureumWebdemo.services;

import com.focusGureumWebApp.focusGureumWebdemo.dto.CategoryRequest;
import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.repository.AppUserRepository;
import com.focusGureumWebApp.focusGureumWebdemo.repository.CategoryRepository;
import org.springframework.security.access.AccessDeniedException;
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
    /*
    function that finds a category and checks if the user is authorized
     */
    public Category getCategoryForUser(Integer categoryId, String nickname) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getNickname().equals(nickname)) {
            throw new AccessDeniedException("User not authorized to access this category");
        }
        return category;
    }

    public List<Category> findAllByUserNickname(String nickname) {
        return categoryRepository.findByUserNickname(nickname);
    }
    public Category createCategory(CategoryRequest categoryRequest, String nickname) {
        AppUser user = appUserRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setImagePath(categoryRequest.getImagePath());
        category.setUser(user);
        categoryRepository.save(category);
        return category;
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public boolean renameCategory(Category category, String newName) {
        try {
            category.setName(newName);
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            // Log error if needed
            return false;
        }
    }

    public boolean changeImage(Category category, String imageUrl) {
        try {
            category.setImagePath(imageUrl);
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            // Log error if needed
            return false;
        }
    }

}
