package com.dios.expensesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response object containing category information")
public class CategoryResponseDTO {

    // ================================
    // FIELDS
    // ================================

    @Schema(description = "Unique identifier of the category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the category", example = "Food & Dining")
    private String name;

    @Schema(description = "Description of the category", example = "Expenses related to meals, restaurants, and food purchases")
    private String description;

    // ================================
    // CONSTRUCTORS
    // ================================

    public CategoryResponseDTO() {
    }

    public CategoryResponseDTO(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // ================================
    // GETTERS AND SETTERS
    // ================================


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    // ================================
    // BUILDER PATTERN
    // ================================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public CategoryResponseDTO build() {
            return new CategoryResponseDTO(id, name, description);
        }
    }
}
