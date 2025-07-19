package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.exception.DuplicateResourceException;
import com.dios.expensesapi.exception.ResourceNotFoundException;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/* Para que no salte el error UnnecessaryStubbingException en el caso de que haya algun stub que no se use */
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

    @Test
    public void create_WithValidCategoryId_ShouldCreateExpense() {

        // Arrange
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        // Act
        ExpenseResponseDTO result = expenseService.create(testExpenseDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testExpenseId);
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("100.50"));
        assertThat(result.getDescription()).isEqualTo("Lunch");
        assertThat(result.getCategory().getName()).isEqualTo("Food");

        verify(categoryRepository).findById(testCategoryId);
        verify(expenseRepository).save(any(Expense.class));
        verify(userService).findByEmail(testUserEmail);
    }

    @Test
    public void create_WithInvalidCategoryId_ShouldThrowResourceNotFoundException() {

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.empty());

        // Comprobar que lanza la excepción que debe
        assertThatThrownBy(() -> expenseService.create(testExpenseDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");

        verify(categoryRepository).findById(testCategoryId);
        // El never() se asegura de que no se haya llamado al método
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void create_WithInvalidCategoryData_ShouldThrowIllegalArgumentException() {

        testExpenseDTO.setCategoryId(null);
        testExpenseDTO.setCategoryName(null);

        assertThatThrownBy(() -> expenseService.create(testExpenseDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Either categoryId or categoryName must be provided");

        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).findByName(any());
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void create_WithDataIntegrityViolation_ShouldThrowDuplicateResourceException() {

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(expenseRepository.save(any(Expense.class))).thenThrow(new DataIntegrityViolationException("Duplicate"));

        assertThatThrownBy(() -> expenseService.create(testExpenseDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Expense");

        verify(categoryRepository).findById(testCategoryId);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void update_WithValidData_ShouldUpdateExpense() {

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(expenseRepository.findByUserAndId(testUser, testExpenseId)).thenReturn(Optional.of(testExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        ExpenseResponseDTO result = expenseService.update(testExpenseId, testExpenseDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testExpenseId);
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("100.50"));

        verify(categoryRepository).findById(testCategoryId);
        verify(expenseRepository).findByUserAndId(testUser, testExpenseId);
        verify(expenseRepository).save(testExpense);
    }

    @Test
    void update_WithNonExistentExpense_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(expenseRepository.findByUserAndId(testUser, testExpenseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.update(testExpenseId, testExpenseDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Expense");

        verify(categoryRepository).findById(testCategoryId);
        verify(expenseRepository).findByUserAndId(testUser, testExpenseId);
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void update_WithInvalidCategory_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.update(testExpenseId, testExpenseDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");

        verify(categoryRepository).findById(testCategoryId);
        verify(expenseRepository, never()).findByUserAndId(any(), any());
    }
}
