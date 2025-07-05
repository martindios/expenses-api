package com.example.jparestful.service;


import com.example.jparestful.dto.CategoryDTO;
import com.example.jparestful.model.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    Iterable<Category> findAll();
    Optional<Category> findById(UUID id);
    Category create(CategoryDTO expenseDTO);
    Category save(Category expense);
    Category update(CategoryDTO expenseDTO);
    void deleteById(UUID id);
}
