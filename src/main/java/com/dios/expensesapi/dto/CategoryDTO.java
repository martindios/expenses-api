package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data to create or update a category")
public class CategoryDTO {

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @Size(max = 1000, message = "Description cannot excedeed 1000 characters")
    private String description;
}
