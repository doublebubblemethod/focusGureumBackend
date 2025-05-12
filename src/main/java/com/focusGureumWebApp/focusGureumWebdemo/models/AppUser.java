package com.focusGureumWebApp.focusGureumWebdemo.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nickname;

    private String avatar;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
