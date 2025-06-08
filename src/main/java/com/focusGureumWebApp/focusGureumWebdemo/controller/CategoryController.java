package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.dto.CategoryRequest;
import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.models.Habit;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import com.focusGureumWebApp.focusGureumWebdemo.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategoriesByUserNickname(Authentication authentication) {
        try {
            String nickname = authentication.getName();
            List<Category> categories = categoryService.findAllByUserNickname(nickname);
            return ResponseEntity.ok(categories); // 200 OK with the list
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving categories: " + e.getMessage());
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(
            Authentication authentication,
            @PathVariable Integer categoryId
    ) {
        try {
            String nickname = authentication.getName();
            Category category = categoryService.getCategoryForUser(categoryId, nickname);
            return ResponseEntity.ok(category);
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/create")
    public ResponseEntity<?> createCategory(Authentication authentication, @RequestBody CategoryRequest categoryRequest) {
        try {
            String nickname = authentication.getName();
            Category createdCategory = categoryService.createCategory(categoryRequest, nickname);
            return ResponseEntity.ok(createdCategory);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating category: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<?> deleteCategory(Authentication authentication, @PathVariable Integer categoryId) {
        try {
            String nickname = authentication.getName();
            Category category = categoryService.getCategoryForUser(categoryId, nickname);
            categoryService.deleteCategory(category);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{categoryId}/rename")
    public ResponseEntity<String> renameCategory(Authentication authentication, @PathVariable Integer categoryId, @RequestBody Map<String, String> body) {
        try {
            String nickname = authentication.getName();
            Category category = categoryService.getCategoryForUser(categoryId, nickname);
            String name = body.get("name");
            try {
                if (name == null || name.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("New category name cannot be empty");
                }
                boolean renamed = categoryService.renameCategory(category, name);

                if (renamed) {
                    return ResponseEntity.ok("Category renamed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Category could not be renamed");
                }
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error renaming category: " + e.getMessage());
            }
    } catch (AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (RuntimeException ex) {
        return ResponseEntity.notFound().build();
    }
    }

    @PatchMapping("/{categoryId}/changeImage")
    public ResponseEntity<?> changeImage(Authentication authentication, @PathVariable Integer categoryId, @RequestBody Map<String, String> body) {
        try {
            String nickname = authentication.getName();
            Category category = categoryService.getCategoryForUser(categoryId, nickname);
            String imageUrl = body.get("imagePath");
            try {
                if (imageUrl == null || imageUrl.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("New category image cannot be empty");
                }
                boolean changedImage = categoryService.changeImage(category, imageUrl);

                if (changedImage) {
                    return ResponseEntity.ok("Category changed Image successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Category could not be changed - Image");
                }
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error changed Image: " + e.getMessage());
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
