package com.focusGureumWebApp.focusGureumWebdemo.models;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "category_FK", nullable = false)
    private Category category;
}
