package com.dios.expensesapi.repository;

import com.dios.expensesapi.model.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, UUID> {
    // Aqui es donde se definen queries personalizadas
}
