package com.dios.expensesapi;

import com.dios.expensesapi.service.ExpenseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpensesAPIApplication {

    private final ExpenseService expenseService;

    public ExpensesAPIApplication(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpensesAPIApplication.class, args);
    }

}
