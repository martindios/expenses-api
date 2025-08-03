package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Schema(description = "Category entity for classifying different types of expenses")
public class Category {

    // ================================
    // FIELDS
    // ================================

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who owns this category")
    private User user;

    // ================================
    // CONSTRUCTORS
    // ================================

    public Category() {
    }

    public Category(UUID id, String name, String description, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        private User user;

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

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Category build() {
            return new Category(id, name, description,  user);
        }
    }
}
