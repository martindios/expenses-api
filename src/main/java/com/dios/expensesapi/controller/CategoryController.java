package com.dios.expensesapi.controller;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Endpoint to manage different types of expense categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<Iterable<CategoryResponseDTO>> findAll() {
        Iterable<CategoryResponseDTO> expenses = categoryService.findAll();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id.toString()));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO saved = categoryService.create(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
