package com.focusGureumWebApp.focusGureumWebdemo.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HabitLog> logs;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HabitSchedule> schedules;
}
