package com.example.jparestful.mapper;

import com.example.jparestful.dto.ExpenseDTO;
import com.example.jparestful.model.Category;
import com.example.jparestful.model.Expense;

public class ExpenseMapper {

    public static Expense toEntity(ExpenseDTO expenseDTO, Category category) {
        return Expense.builder()
                .expenseDate(expenseDTO.getExpenseDate())
                .category(category)
                .amount(expenseDTO.getAmount())
                .description(expenseDTO.getDescription())
                .build();
    }

    public static ExpenseDTO toDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        expenseDTO.setCategoryId(expense.getCategory().getId());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setDescription(expense.getDescription());
        return expenseDTO;
    }

}
