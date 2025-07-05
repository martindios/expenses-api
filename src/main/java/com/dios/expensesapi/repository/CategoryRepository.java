package com.dios.expensesapi.repository;

import com.dios.expensesapi.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends CrudRepository<Category, UUID> {
    Optional<Category> findByName(String name);
}
