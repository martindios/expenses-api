package com.example.jparestful.service;

import com.example.jparestful.dto.ExpenseDTO;
import com.example.jparestful.exception.DuplicateResourceException;
import com.example.jparestful.exception.ResourceNotFoundException;
import com.example.jparestful.mapper.ExpenseMapper;
import com.example.jparestful.model.Category;
import com.example.jparestful.model.Expense;
import com.example.jparestful.repository.CategoryRepository;
import com.example.jparestful.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional // Asegura la consistencia de las operaciones que modifican datos, rollback o commit
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Iterable<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return expenseRepository.findById(id);
    }

    @Override
    public Expense create(ExpenseDTO expenseDTO) {
        Category category = findCategory(expenseDTO);

        try {
            Expense expense = ExpenseMapper.toEntity(expenseDTO, category);
            return expenseRepository.save(expense);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Expense", "category", category.getName());
        }

    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Expense update(UUID id, ExpenseDTO expenseDTO) {
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category"));
        return expenseRepository.findById(id)
                .map(existing -> {
                    existing.setExpenseDate(expenseDTO.getExpenseDate());
                    existing.setCategory(category);
                    existing.setAmount(expenseDTO.getAmount());
                    existing.setDescription(expenseDTO.getDescription());
                    return expenseRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id.toString()));
    }

    @Override
    public void deleteById(UUID id) {
        if(!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense", id.toString());
        }
        expenseRepository.deleteById(id);
    }

    private Category findCategory(ExpenseDTO expenseDTO) {
        if(!expenseDTO.hasValidCategory()) {
            throw new IllegalArgumentException("Either categoryId or categoryName must be provided");
        }

        if(expenseDTO.getCategoryId() != null) {
            return categoryRepository.findById(expenseDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id " + expenseDTO.getCategoryId()));
        }

        return categoryRepository.findByName(expenseDTO.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category", expenseDTO.getCategoryName()));
    }
}
