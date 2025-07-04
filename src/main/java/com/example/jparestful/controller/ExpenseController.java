package com.example.jparestful.controller;

import com.example.jparestful.dto.ExpenseDTO;
import com.example.jparestful.mapper.ExpenseMapper;
import com.example.jparestful.model.Expense;
import com.example.jparestful.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Expense> create(@Valid @RequestBody ExpenseDTO dto) {
        Expense expense = expenseService.create(dto);
        Expense saved = expenseService.save(expense);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable UUID id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        try {
            Expense updated = expenseService.update(id, expenseDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            expenseService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
