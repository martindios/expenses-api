package com.dios.expensesapi.repository;

import com.dios.expensesapi.model.Expense;
import com.dios.expensesapi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, UUID> {
    Iterable<Expense> findByUser(User user);
    Optional<Expense> findByUserAndId(User user, UUID id);
    // Aqui es donde se definen queries personalizadas

}
