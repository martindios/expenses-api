package com.dios.expensesapi.dto;


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
public class ExpenseDTO {

    @NotNull(message = "Date is mandatory")
    private LocalDateTime expenseDate;

    // No se le puede poner NotNull porque uno de ellos si que puede ser null, se utiliza el método hasValidCategory
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
