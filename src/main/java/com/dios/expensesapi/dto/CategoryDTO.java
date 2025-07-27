package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data transfer object for creating or updating a category")
public class CategoryDTO {

    // ================================
    // FIELDS
    // ================================

    @NotBlank(message = "Category name cannot be blank")
    @Schema(description = "Name of the category", example = "Food & Dining")
    private String name;

    @Size(max = 1000, message = "Description cannot excedeed 1000 characters")
    @Schema(description = "Detailed description of the category", example = "Expenses related to meals, restaurants, and food purchases")
    private String description;

    // ================================
    // GETTERS AND SETTERS
    // ================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
