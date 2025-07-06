package com.dios.expensesapi.mapper;

import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.model.Expense;

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

    public static ExpenseResponseDTO toResponseDTO(Expense expense) {
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .userId(expense.getUserId())
                .expenseDate(expense.getExpenseDate())
                .category(CategoryMapper.toResponseDTO(expense.getCategory()))
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .createdAt(expense.getCreatedAt())
                .build();
    }

}
