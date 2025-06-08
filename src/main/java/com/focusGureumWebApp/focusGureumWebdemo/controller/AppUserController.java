package com.focusGureumWebApp.focusGureumWebdemo.controller;
import com.focusGureumWebApp.focusGureumWebdemo.dto.AuthRequest;
import com.focusGureumWebApp.focusGureumWebdemo.models.AppUser;
import com.focusGureumWebApp.focusGureumWebdemo.services.AppUserService;
import com.focusGureumWebApp.focusGureumWebdemo.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody AppUser appUser) {
        return service.addUser(appUser);
    }

    // Removed the role checks here as they are already managed in SecurityConfig
    /*
        {
          "nickname": "focus_user",
          "password": "SecureP@ssw0rd"
        }
     */
    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getNickname(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getNickname());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (Exception e) {
            e.printStackTrace(); // Log exception stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication error");
        }
    }

    @GetMapping("/user/userProfile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        // Access authenticated user info from SecurityContext
        String username = authentication.getName();
        // You can also cast authentication.getPrincipal() to your user details class if needed
        return ResponseEntity.ok("User Profile for " + username);
    }

    @GetMapping("/admin/userProfile")
    public ResponseEntity<?> getAdminUserProfile(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            // Return 403 Forbidden if the user is not an admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: Admin role required");
        }

        String username = authentication.getName();
        // Your admin profile logic here

        return ResponseEntity.ok("Admin Profile for " + username);
    }
}