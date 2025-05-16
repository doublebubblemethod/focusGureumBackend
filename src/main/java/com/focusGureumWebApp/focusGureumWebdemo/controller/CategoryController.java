package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public String showCategoriesHome(Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "categories";
    }
    @PostMapping
    public String createCategory(@RequestParam String name,
                                 @RequestParam boolean status,
                                 @RequestParam String imagePath,
                                 @RequestParam Integer userId) {
        categoryService.createCategory(name, status, imagePath, userId);
        return "redirect:/categories/user/" + userId;
    }
    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/toggle")
    public String toggleCategory(@PathVariable Integer id) {
        categoryService.toggleCategory(id);
        return "redirect:/categories";
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

