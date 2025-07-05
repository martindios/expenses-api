package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.model.Expense;

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
