package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for creating or updating a category")
public class CategoryDTO {

    @NotBlank(message = "Category name cannot be blank")
    @Schema(description = "Name of the category", example = "Food & Dining")
    private String name;

    @Size(max = 1000, message = "Description cannot excedeed 1000 characters")
    @Schema(description = "Detailed description of the category", example = "Expenses related to meals, restaurants, and food purchases")
    private String description;
}
