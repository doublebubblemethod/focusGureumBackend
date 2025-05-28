package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.models.Task;
import com.focusGureumWebApp.focusGureumWebdemo.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public ResponseEntity<?> showCategoriesHome() {
        try {
            List<Category> categories = categoryService.getAll();
            return ResponseEntity.ok(categories); // 200 OK with the list
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving categories: " + e.getMessage());
        }
    }
    @PatchMapping("/{id}/name")
    public ResponseEntity<?> createCategory(@RequestBody Map<String, Object> body) {
        try {
            String name = (String) body.get("name");
            Boolean status = (Boolean) body.get("status");
            String imagePath = (String) body.get("imagePath");
            Integer userId = (Integer) body.get("userId");

            // Basic validation
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Category name cannot be empty");
            }
            if (userId == null) {
                return ResponseEntity.badRequest().body("User ID is required");
            }

            categoryService.createCategory(name, status, imagePath, userId);
            return ResponseEntity.ok("Category created successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating category: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting category: " + e.getMessage());
        }
    }
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleCategory(@PathVariable Integer id) {
        try {
            boolean isToggled = categoryService.toggleCategory(id);
            if (isToggled) {
                return ResponseEntity.ok("Category toggled successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Category could not be toggled");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling category: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public String getCategoryService(@PathVariable Integer userId, Model model) {
        List<Category> categories = categoryService.getAllByUser_Id(userId);
        model.addAttribute("categories", categories);
        return "categories";
    }
}
//    @GetMapping("/{id}/edit")
//    public String showEditCategoryForm(@PathVariable Integer id, Model model) {
//        Category category = categoryService.getById(id);
//        model.addAttribute("category", category);
//        return "editCategory";
//}

