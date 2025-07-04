package com.example.jparestful.service;

import com.example.jparestful.dto.ExpenseDTO;
import com.example.jparestful.model.Expense;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseService {
    Iterable<Expense> findAll();
    Optional<Expense> findById(UUID id);
    Expense create(ExpenseDTO expenseDTO);
    Expense save(Expense expense);
    Expense update(UUID id, ExpenseDTO expenseDTO);
    void deleteById(UUID id);
}
