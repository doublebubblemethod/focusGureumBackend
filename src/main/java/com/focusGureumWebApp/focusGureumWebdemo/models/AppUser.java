package com.focusGureumWebApp.focusGureumWebdemo.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nickname;
    private String avatar;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    private String email;
    private String password;
    private String role;
}
