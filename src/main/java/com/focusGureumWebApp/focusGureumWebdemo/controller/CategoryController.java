package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.Category;
import com.focusGureumWebApp.focusGureumWebdemo.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{userId}")
    public String getCategoryService(@PathVariable Integer userId, Model model) {
        List<Category> categories = categoryService.getAllByUser_Id(userId);
        model.addAttribute("categories", categories);
        return "categories";
    }
}
