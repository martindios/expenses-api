package com.dios.expensesapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Response object containing complete expense information")
public class ExpenseResponseDTO {

    // ================================
    // FIELDS
    // ================================

    @Schema(description = "Unique identifier of the expense", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "ID of the user who owns this expense", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Date when the expense occurred", example = "15-01-2024 14:30:00")
    private LocalDateTime expenseDate;

    @Schema(description = "Category information for this expense")
    private CategoryResponseDTO category;

    @Schema(description = "Amount of the expense", example = "150.75")
    private BigDecimal amount;

    @Schema(description = "Description or notes about the expense", example = "Grocery shopping at local market")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Timestamp when the expense was created", example = "15-01-2024 10:30:00")
    private LocalDateTime createdAt;

    // ================================
    // CONSTRUCTORS
    // ================================

    public ExpenseResponseDTO() {
    }

    public ExpenseResponseDTO(UUID id, UUID userId, LocalDateTime expenseDate, CategoryResponseDTO category, BigDecimal amount, String description, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.expenseDate = expenseDate;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public CategoryResponseDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseDTO category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ================================
    // BUILDER PATTERN
    // ================================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID userId;
        private LocalDateTime expenseDate;
        private CategoryResponseDTO category;
        private BigDecimal amount;
        private String description;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder expenseDate(LocalDateTime expenseDate) {
            this.expenseDate = expenseDate;
            return this;
        }

        public Builder category(CategoryResponseDTO category) {
            this.category = category;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ExpenseResponseDTO build() {
            return new ExpenseResponseDTO(
                    id,
                    userId,
                    expenseDate,
                    category,
                    amount,
                    description,
                    createdAt
            );
        }
    }
}
