package com.dios.expensesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // JPA entity, annotated to be mapped to a database table
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Generates a builder for the class to create instances using the builder pattern
@Schema(description = "Expense entity representing a user's financial expense record")
public class Expense {

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

    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

}
