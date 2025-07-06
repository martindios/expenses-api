package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.model.Expense;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseService {
    Iterable<ExpenseResponseDTO> findAll();
    Optional<ExpenseResponseDTO> findById(UUID id);
    ExpenseResponseDTO create(ExpenseDTO expenseDTO);
    ExpenseResponseDTO update(UUID id, ExpenseDTO expenseDTO);
    void deleteById(UUID id);
}
