package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Iterable<CategoryResponseDTO> findAll();
    Page<CategoryResponseDTO> findAll(Pageable pageable);
    Page<CategoryResponseDTO> findByNameContaining(String name, Pageable pageable);
    Optional<CategoryResponseDTO> findById(UUID id);
    CategoryResponseDTO create(CategoryDTO expenseDTO);
    CategoryResponseDTO update(UUID id, CategoryDTO expenseDTO);
    void deleteById(UUID id);
}
