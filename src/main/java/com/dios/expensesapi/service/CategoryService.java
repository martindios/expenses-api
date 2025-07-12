package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Iterable<CategoryResponseDTO> findAll();
    Optional<CategoryResponseDTO> findById(UUID id);
    CategoryResponseDTO create(CategoryDTO expenseDTO);
    CategoryResponseDTO update(UUID id, CategoryDTO expenseDTO);
    void deleteById(UUID id);
}
