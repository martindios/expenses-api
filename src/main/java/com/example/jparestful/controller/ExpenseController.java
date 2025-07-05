package com.example.jparestful.controller;

import com.example.jparestful.dto.ExpenseDTO;
import com.example.jparestful.exception.ResourceNotFoundException;
import com.example.jparestful.mapper.ExpenseMapper;
import com.example.jparestful.model.Expense;
import com.example.jparestful.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Expense>> findAll() {
        Iterable<Expense> expenses = expenseService.findAll();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> findById(@PathVariable UUID id) {
        return expenseService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id.toString()));
    }

    @PostMapping
    public ResponseEntity<Expense> create(@Valid @RequestBody ExpenseDTO dto) {
        Expense saved = expenseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable UUID id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        Expense updated = expenseService.update(id, expenseDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
