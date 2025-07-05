package com.example.jparestful.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @Size(max = 1000, message = "Description cannot excedeed 1000 characters")
    private String description;
}
