package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // JPA entity, annotated to be mapped to a database table
@Table(name = "expenses")
@Schema(description = "Expense entity representing a user's financial expense record")
public class Expense {

    // ================================
    // FIELDS
    // ================================

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier for the expense", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who owns this expense")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Date when the expense occurred", example = "01-01-2001 01:01:01")
    private LocalDateTime expenseDate;

    @ManyToOne(fetch = FetchType.EAGER) // Many expenses can belong to one category
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "Category classification for this expense")
    private Category category;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Amount of the expense", example = "150.75")
    private BigDecimal amount;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Optional description or notes about the expense", example = "Grocery shopping at local market")
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the expense record was created", example = "01-01-2001 01:01:01")
    private LocalDateTime createdAt;

    // ================================
    // CONSTRUCTORS
    // ================================

    public Expense() {
    }

    public Expense(UUID id, User user, LocalDateTime expenseDate, Category category, BigDecimal amount, String description, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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

    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

    // ================================
    // BUILDER PATTERN
    // ================================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private User user;
        private LocalDateTime expenseDate;
        private Category category;
        private BigDecimal amount;
        private String description;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder expenseDate(LocalDateTime expenseDate) {
            this.expenseDate = expenseDate;
            return this;
        }

        public Builder category(Category category) {
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

        public Expense build() {
            return new Expense(
                    id,
                    user,
                    expenseDate,
                    category,
                    amount,
                    description,
                    createdAt
            );
        }
    }
}

