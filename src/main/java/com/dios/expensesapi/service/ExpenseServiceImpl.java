package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.exception.DuplicateResourceException;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.mapper.ExpenseMapper;
import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.model.Expense;
import com.dios.expensesapi.model.User;
import com.dios.expensesapi.repository.CategoryRepository;
import com.dios.expensesapi.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional // Asegura la consistencia de las operaciones que modifican datos, rollback o commit
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }


    @Override
    public Iterable<Expense> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        return expenseRepository.findByUser(currentUser);
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return expenseRepository.findById(id);
    }

    @Override
    public Expense create(ExpenseDTO expenseDTO) {
        Category category = findCategory(expenseDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        try {
            Expense expense = ExpenseMapper.toEntity(expenseDTO, category);
            expense.setUser(currentUser);
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        return expenseRepository.findByUserAndId(currentUser, id)
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userService.findByEmail(userEmail);

        for(Expense expense : expenseRepository.findByUser(currentUser)) {
            if(expense.getId().equals(id)) {
                expenseRepository.deleteById(id);
                break;
            }
        }
    }

    private Category findCategory(ExpenseDTO expenseDTO) {
        if(!expenseDTO.hasValidCategory()) {
            throw new IllegalArgumentException("Either categoryId or categoryName must be provided");
        }

        if(expenseDTO.getCategoryId() != null) {
            return categoryRepository.findById(expenseDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", expenseDTO.getCategoryId().toString()));
        }

        return categoryRepository.findByName(expenseDTO.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category", expenseDTO.getCategoryName()));
    }
}
