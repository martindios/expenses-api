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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing complete expense information")
public class ExpenseResponseDTO {

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
}
