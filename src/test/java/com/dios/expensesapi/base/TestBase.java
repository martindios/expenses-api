package com.dios.expensesapi.base;

import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.model.Expense;
import com.dios.expensesapi.model.Role;
import com.dios.expensesapi.model.User;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@ActiveProfiles("test")
public abstract class TestBase {

    protected static final String TEST_EMAIL = "test@example.com";
    protected static final String TEST_PASSWORD = "password123";
    protected static final String TEST_FIRST_NAME = "John";
    protected static final String TEST_LAST_NAME = "Doe";

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected User createTestUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .role(Role.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    protected User createTestAdmin() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("admin@example.com")
                .password(TEST_PASSWORD)
                .firstName("Admin")
                .lastName("User")
                .role(Role.ADMIN)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    protected Category createTestCategory() {
        return Category.builder()
                .id(UUID.randomUUID())
                .name("Test Category")
                .description("Test Category description")
                .build();
    }

    protected Expense createTestExpense(User user, Category category) {
        return Expense.builder()
                .id(UUID.randomUUID())
                .user(user)
                .category(category)
                .amount(new BigDecimal("100.50"))
                .description("Test Expense description")
                .expenseDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    protected String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
