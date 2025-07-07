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
@Schema(description = "Data to create or update an expense")
public class ExpenseDTO {

    @NotNull(message = "Date is mandatory")
    private LocalDateTime expenseDate;

    // Cannot use NotNull because one of them can be null; the hasValidCategory() is used
    private UUID categoryId;
    private String categoryName;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be higher than 0")
    @Digits(integer = 8, fraction = 2, message = "Price with up to 8 integer digits and 2 decimal places")
    private BigDecimal amount;

    @Size(max = 1000, message = "Description cannot excedeed 1000 characters")
    private String description;

    @AssertTrue(message = "Either categoryID or categoryName must be provided")
    public boolean hasValidCategory() {
        return categoryId != null || (categoryName != null && !categoryName.trim().isEmpty());
    }
}
