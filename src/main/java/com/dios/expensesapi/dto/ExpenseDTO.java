package com.dios.expensesapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Validated
@Schema(description = "Data transfer object for creating or updating an expense")
public class ExpenseDTO {

    @NotNull(message = "Date is mandatory")
    @Schema(description = "Date when the expense occurred", example = "01-01-2001 01:01:01")
    private LocalDateTime expenseDate;

    // Cannot use NotNull because one of them can be null; the hasValidCategory() is used
    @Schema(description = "UUID of the expense category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID categoryId;
    @Schema(description = "Name of the expense category (alternative to categoryId)", example = "Food & Dining")
    private String categoryName;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be higher than 0")
    @Digits(integer = 8, fraction = 2, message = "Price with up to 8 integer digits and 2 decimal places")
    @Schema(description = "Amount of the expense", example = "150.75")
    private BigDecimal amount;

    @Size(max = 1000, message = "Description cannot excedeed 1000 characters")
    @Schema(description = "Optional description or notes about the expense", example = "Grocery shopping at local market")
    private String description;

    @AssertTrue(message = "Either categoryID or categoryName must be provided")
    public boolean hasValidCategory() {
        return categoryId != null || (categoryName != null && !categoryName.trim().isEmpty());
    }
}
