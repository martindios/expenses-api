package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing category information")
public class CategoryResponseDTO {

    @Schema(description = "Unique identifier of the category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the category", example = "Food & Dining")
    private String name;

    @Schema(description = "Description of the category", example = "Expenses related to meals, restaurants, and food purchases")
    private String description;
}
