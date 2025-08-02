package com.dios.expensesapi.controller;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.dto.error.ErrorResponse;
import com.dios.expensesapi.dto.error.ValidationErrorResponse;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.service.CategoryService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Operations related to category management. Provides" +
        " endpoints to handle different types of expense classifications.")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList("id", "name", "description");
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Get all categories with pagination and search",
            description = """
                    Retrieve a paginated list of expense categories for the authenticated user.
                    Supports filtering by name and flexible sorting options.
                    
                    **Usage Examples:**
                    - Get first 10 categories: `/api/v1/categories`
                    - Search categories: `/api/v1/categories?search=food`
                    - Custom pagination: `/api/v1/categories?page=1&size=20&sortBy=name&sortDir=desc`
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved paginated categories",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))
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
    public ResponseEntity<Page<CategoryResponseDTO>> findAll(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(defaultValue = "0") @Min(0) int page,

        @Parameter(description = "Number of items per page (1-100)", example = "10")
        @RequestParam(defaultValue = "10") @Min(1) @Max(MAX_PAGE_SIZE) int size,

        @Parameter(description = "Sort field. Valid values: id, name, description", example = "name")
        @RequestParam(defaultValue = "name") String sortBy,

        @Parameter(description = "Sort direction", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
        @RequestParam(defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$",
                message = "Sort direction must be 'asc' or 'desc'") String sortDir,

        @Parameter(description = "Search term to filter categories by name", example = "food")
        @RequestParam(required = false) String search
    ) {

        if(!VALID_SORT_FIELDS.contains(sortBy)) {
            sortBy = "name";
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<CategoryResponseDTO> categoriesPage;

        if(search != null && !search.isEmpty()) {
            categoriesPage = categoryService.findByNameContaining(search.trim(), pageable);
        } else {
            categoriesPage = categoryService.findAll(pageable);
        }

        return ResponseEntity.ok(categoriesPage);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a specific category by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format",
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id.toString()));
    }

    @Operation(
            summary = "Create a new category",
            description = "Create a new expense category with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDTO.class)
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
                    responseCode = "409",
                    description = "Category already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO saved = categoryService.create(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
            summary = "Update an existing category",
            description = "Update the details of an existing category identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDTO.class)
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
                    description = "Category not found",
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
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a category",
            description = "Delete an existing category by its ID. This operation cannot be undone."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
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
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
