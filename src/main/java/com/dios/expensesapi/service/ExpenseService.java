package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseService {
    Iterable<ExpenseResponseDTO> findAll();
    Page<ExpenseResponseDTO> findAll(Pageable pageable);
    Page<ExpenseResponseDTO> findByCategoryName(String categoryName, Pageable pageable);
    Optional<ExpenseResponseDTO> findById(UUID id);
    ExpenseResponseDTO create(ExpenseDTO expenseDTO);
    ExpenseResponseDTO update(UUID id, ExpenseDTO expenseDTO);
    void deleteById(UUID id);
}
