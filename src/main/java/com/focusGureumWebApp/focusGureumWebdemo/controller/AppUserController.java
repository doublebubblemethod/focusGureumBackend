package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.services.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
    @GetMapping
    public String getAppUserService(Model model) {
        List<AppUser> users = appUserService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
