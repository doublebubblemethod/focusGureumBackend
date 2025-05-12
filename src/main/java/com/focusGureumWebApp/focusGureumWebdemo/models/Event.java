package com.focusGureumWebApp.focusGureumWebdemo.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_timestamp", nullable = false)
    private LocalDateTime startTimestamp;

    @Column(name = "end_timestamp")
    private LocalDateTime endTimestamp;

    @Column(name = "recurrence_type", nullable = false)
    private String recurrenceType;

    @Column(name = "recurrence_step")
    private String recurrenceStep;

    @Column(name = "recurrence_days")
    private String recurrenceDays;

    @Column(name = "recurrence_finish")
    private LocalDate recurrenceFinish;

    @Column(name = "tag_color")
    private String tagColor;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // Getters, setters, constructors
}
