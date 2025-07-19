package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.model.Expense;
import com.dios.expensesapi.model.User;
import com.dios.expensesapi.repository.CategoryRepository;
import com.dios.expensesapi.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

    /* Creación de objetos simulados de las dependencias */
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks // Inyecta los mocks en su constructor
    private ExpenseServiceImpl expenseService;

    private User testUser;
    private Category testCategory;
    private Expense testExpense;
    private ExpenseDTO  testExpenseDTO;
    private final UUID testUserId = UUID.randomUUID();
    private final UUID testCategoryId = UUID.randomUUID();
    private final UUID testExpenseId = UUID.randomUUID();
    private final String testUserEmail = "test@example.com";

    @BeforeEach // Se ejecuta antes de cada método de prueba @Test
    void setUp() {
        testUser = User.builder()
                .id(testUserId)
                .email(testUserEmail)
                .firstName("John")
                .lastName("Doe")
                .build();

        testCategory = Category.builder()
                .id(testCategoryId)
                .name("Food")
                .description("Food expenses")
                .build();

        testExpense = Expense.builder()
                .id(testExpenseId)
                .user(testUser)
                .expenseDate(LocalDateTime.now())
                .category(testCategory)
                .amount(new BigDecimal("100.50"))
                .description("Lunch")
                .createdAt(LocalDateTime.now())
                .build();

        testExpenseDTO = new ExpenseDTO();
        testExpenseDTO.setExpenseDate(LocalDateTime.now());
        testExpenseDTO.setCategoryId(testCategoryId);
        testExpenseDTO.setAmount(new BigDecimal("100.50"));
        testExpenseDTO.setDescription("Lunch");

        // Mock security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(testUserEmail);
        when(userService.findByEmail(testUserEmail)).thenReturn(testUser);
    }

    @Test
    public void findAll_ShouldReturnAllExpensesForCurrentUser() {

        // Arrange
        List<Expense> expenses = Arrays.asList(testExpense);
        when(expenseRepository.findByUser(testUser)).thenReturn(expenses);

        // Act
        Iterable<ExpenseResponseDTO> result = expenseService.findAll();

        // Assert
        assertThat(result).isNotNull();
        List<ExpenseResponseDTO> resultList = (List<ExpenseResponseDTO>) result;
        assertThat(resultList).hasSize(1);

        ExpenseResponseDTO responseDTO = resultList.getFirst();
        assertThat(responseDTO.getId()).isEqualTo(testExpenseId);
        assertThat(responseDTO.getUserId()).isEqualTo(testUserId);
        assertThat(responseDTO.getAmount()).isEqualTo(new BigDecimal("100.50"));
        assertThat(responseDTO.getDescription()).isEqualTo("Lunch");
        assertThat(responseDTO.getCategory().getName()).isEqualTo("Food");

        /* Se verifica que durante la ejecución del expenseService.findAll() se invocaron
         los siguientes métodos con los siguientes atributos */
        verify(expenseRepository).findByUser(testUser);
        verify(userService).findByEmail(testUserEmail);
    }

    @Test
    public void findById_WhenExpenseExists_ShouldReturnExpenseResponseDTO() {

        // Arrange
        // Se coloca .findByUserAndId() porque .findById() lo utiliza en su impl
        when(expenseRepository.findByUserAndId(testUser, testExpenseId)).thenReturn(Optional.of(testExpense));

        // Act
        Optional<ExpenseResponseDTO> result = expenseService.findById(testExpenseId);

        // Assert
        assertThat(result).isPresent(); // Así se comprueba que el Optional contiene un valor, es decir, que no contiene un Optional.empty()
        ExpenseResponseDTO responseDTO = result.get();
        assertThat(responseDTO.getId()).isEqualTo(testExpenseId);
        assertThat(responseDTO.getAmount()).isEqualTo(new BigDecimal("100.50"));
        assertThat(responseDTO.getDescription()).isEqualTo("Lunch");

        verify(expenseRepository).findByUserAndId(testUser, testExpenseId);
        verify(userService).findByEmail(testUserEmail);

    }

    @Test
    public void findById_WhenExpenseDoesNotExists_ShouldReturnEmptyOptional() {

        // Arrange
        when(expenseRepository.findByUserAndId(testUser, testExpenseId)).thenReturn(Optional.empty());

        // Act
        Optional<ExpenseResponseDTO> result = expenseService.findById(testExpenseId);

        // Assert
        assertThat(result).isEmpty();

        verify(expenseRepository).findByUserAndId(testUser, testExpenseId);
        verify(userService).findByEmail(testUserEmail);

    }


}
