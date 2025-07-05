package com.dios.expensesapi.service;


import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.model.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Iterable<Category> findAll();
    Optional<Category> findById(UUID id);
    Category create(CategoryDTO expenseDTO);
    Category save(Category expense);
    Category update(UUID id, CategoryDTO expenseDTO);
    void deleteById(UUID id);
}
