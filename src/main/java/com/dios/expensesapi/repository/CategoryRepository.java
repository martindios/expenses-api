package com.dios.expensesapi.repository;

import com.dios.expensesapi.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends CrudRepository<Category, UUID>, PagingAndSortingRepository<Category, UUID> {
    Optional<Category> findByName(String name);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
