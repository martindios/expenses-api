package com.example.jparestful;

import com.example.jparestful.service.ExpenseService;
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
