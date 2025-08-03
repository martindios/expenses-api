package com.dios.expensesapi.controller;

import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.dto.PagedResponse;
import com.dios.expensesapi.dto.error.ErrorResponse;
import com.dios.expensesapi.dto.error.ValidationErrorResponse;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expenses", description = "Operations related to expense managment. Allows users to create, read, update and delete their personal expenses.")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(
            summary = "Get all expenses with pagination and search",
            description = """
                    Retrieve a paginated list of expenses for the authenticated user.
                    Supports filtering by category and flexible sorting options.
                    
                    **Usage Examples:**
                    - Get first 10 expenses: `/api/expenses`
                    - Search expenses: `/api/expenses?search=food`
                    - Custom pagination: `/api/expenses?page=1&size=20&sortBy=cateogory&sortDir=desc`
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved expenses",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ExpenseResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PagedResponse<ExpenseResponseDTO>> findAll(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Number of items per page (1-100)", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,

            @Parameter(description = "Sort field. Valid values: category, description", example = "category")
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(description = "Sort direction", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$",
                    message = "Sort direction must be 'asc' or 'desc'") String sortDir,

            @Parameter(description = "Search term to filter expenses by category", example = "food")
            @RequestParam(required = false) String search
    ) {
        List<String> validSortFields = Arrays.asList("category", "description");
        if (!validSortFields.contains(sortBy)) {
            sortBy = "category";
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ExpenseResponseDTO> expensesPage;

        if(search != null && !search.isEmpty()) {
            expensesPage = expenseService.findByCategoryName(search.trim(), pageable);
        } else {
            expensesPage = expenseService.findAll(pageable);
        }

        PagedResponse<ExpenseResponseDTO> response = new PagedResponse<>(expensesPage);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get expense by ID",
            description = "Retrieve a specific expense by its unique identifier, including all associated details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expense found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Expense not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> findById(@PathVariable UUID id) {
        return expenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id.toString()));
    }

    @Operation(
            summary = "Create a new expense",
            description = "Create a new expense record with the provided details including amount, description, date, and category."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Expense created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation errors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Referenced category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> create(@Valid @RequestBody ExpenseDTO dto) {
        ExpenseResponseDTO saved = expenseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
            summary = "Update an existing expense",
            description = "Update the details of an existing expense identified by its ID. All fields in the request body will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expense updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation errors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Expense or category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        ExpenseResponseDTO updated = expenseService.update(id, expenseDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete an expense",
            description = "Delete an existing expense by its ID. This operation cannot be undone and will permanently remove the expense record."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Expense deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Expense not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
