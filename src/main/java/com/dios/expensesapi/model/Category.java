package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Category entity for classifying different types of expenses")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier for the category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Name of the category", example = "Dining")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Detailed description of the category", example = "Expenses related to meals, restaurants, and food purchases")
    private String description;
}
