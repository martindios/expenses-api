package com.dios.expensesapi;

import com.dios.expensesapi.service.ExpenseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaRestfulApplication {

    private final ExpenseService expenseService;

    public JpaRestfulApplication(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(JpaRestfulApplication.class, args);
    }

}
