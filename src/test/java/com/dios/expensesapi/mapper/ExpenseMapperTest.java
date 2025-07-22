package com.dios.expensesapi.mapper;

import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.model.Expense;
import com.dios.expensesapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ExpenseMapperTest {

    private User testUser;
    private Category testCategory;
    private Expense testExpense;
    private ExpenseDTO testExpenseDTO;
    private CategoryResponseDTO testCategoryResponseDTO;

    private final UUID testUserId = UUID.randomUUID();
    private final UUID testCategoryId = UUID.randomUUID();
    private final UUID testExpenseId = UUID.randomUUID();
    private final LocalDateTime testExpenseDate = LocalDateTime.of(2024, 1, 15, 10, 30);
    private final LocalDateTime testCreatedAt = LocalDateTime.of(2024, 1, 15, 10, 35);
    private final BigDecimal testAmount = new BigDecimal("125.75");
    private final String testDescription = "Lunch at restaurant";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(testUserId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        testCategory = Category.builder()
                .id(testCategoryId)
                .name("Food")
                .description("Food expenses")
                .build();

        testCategoryResponseDTO = CategoryResponseDTO.builder()
                .id(testCategoryId)
                .name("Food")
                .description("Food expenses")
                .build();

        testExpense = Expense.builder()
                .id(testExpenseId)
                .user(testUser)
                .expenseDate(testExpenseDate)
                .category(testCategory)
                .amount(testAmount)
                .description(testDescription)
                .createdAt(testCreatedAt)
                .build();

        testExpenseDTO = new ExpenseDTO();
        testExpenseDTO.setExpenseDate(testExpenseDate);
        testExpenseDTO.setCategoryId(testCategoryId);
        testExpenseDTO.setAmount(testAmount);
        testExpenseDTO.setDescription(testDescription);
    }

    @Test
    void toEntity_WithValidData_ShouldCreateExpenseEntity() {
        // Act
        Expense result = ExpenseMapper.toEntity(testExpenseDTO, testCategory);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
        assertThat(result.getCategory()).isEqualTo(testCategory);
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isEqualTo(testDescription);

        // Verify that fields not set in DTO are null (id, user, createdAt)
        assertThat(result.getId()).isNull();
        assertThat(result.getUser()).isNull();
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void toEntity_WithNullExpenseDate_ShouldCreateEntityWithNullDate() {
        // Arrange
        testExpenseDTO.setExpenseDate(null);

        // Act
        Expense result = ExpenseMapper.toEntity(testExpenseDTO, testCategory);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isNull();
        assertThat(result.getCategory()).isEqualTo(testCategory);
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isEqualTo(testDescription);
    }

    @Test
    void toEntity_WithNullDescription_ShouldCreateEntityWithNullDescription() {
        // Arrange
        testExpenseDTO.setDescription(null);

        // Act
        Expense result = ExpenseMapper.toEntity(testExpenseDTO, testCategory);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
        assertThat(result.getCategory()).isEqualTo(testCategory);
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isNull();
    }

    @Test
    void toEntity_WithNullCategory_ShouldCreateEntityWithNullCategory() {
        // Act
        Expense result = ExpenseMapper.toEntity(testExpenseDTO, null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
        assertThat(result.getCategory()).isNull();
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isEqualTo(testDescription);
    }

    @Test
    void toDTO_WithValidExpense_ShouldCreateExpenseDTO() {
        // Act
        ExpenseDTO result = ExpenseMapper.toDTO(testExpense);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
        assertThat(result.getCategoryId()).isEqualTo(testCategoryId);
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isEqualTo(testDescription);

        // Verify that categoryName is null (not set in mapper)
        assertThat(result.getCategoryName()).isNull();
    }


    @Test
    void toDTO_WithNullExpenseDate_ShouldCreateDTOWithNullDate() {
        // Arrange
        testExpense.setExpenseDate(null);

        // Act
        ExpenseDTO result = ExpenseMapper.toDTO(testExpense);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isNull();
        assertThat(result.getCategoryId()).isEqualTo(testCategoryId);
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isEqualTo(testDescription);
    }

    @Test
    void toDTO_WithNullDescription_ShouldCreateDTOWithNullDescription() {
        testExpense.setDescription(null);

        ExpenseDTO result = ExpenseMapper.toDTO(testExpense);

        assertThat(result).isNotNull();
        assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
        assertThat(result.getCategoryId()).isEqualTo(testCategoryId);
        assertThat(result.getAmount()).isEqualTo(testAmount);
        assertThat(result.getDescription()).isNull();
    }


    @Test
    void toResponseDTO_WithValidExpense_ShouldCreateExpenseResponseDTO() {
        /*
         * Use try-with-resources syntax to create a temporary static mock of CategoryMapper:
         * - mockStatic(CategoryMapper.class) returns a MockedStatic that implements AutoCloseable.
         * - Wrapping it in try(...) ensures the mock is automatically closed (and real behavior restored)
         *   when the block exits, preventing side effects in other tests.
         * We mock CategoryMapper.toResponseDTO(...) so that:
         * 1. We isolate this unit test to only verify ExpenseMapper’s logic.
         * 2. We avoid pulling in CategoryMapper’s implementation or dependencies.
         */
        // Arrange
        try (MockedStatic<CategoryMapper> categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.toResponseDTO(testCategory))
                    .thenReturn(testCategoryResponseDTO);

            // Act
            ExpenseResponseDTO result = ExpenseMapper.toResponseDTO(testExpense);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testExpenseId);
            assertThat(result.getUserId()).isEqualTo(testUserId);
            assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
            assertThat(result.getCategory()).isEqualTo(testCategoryResponseDTO);
            assertThat(result.getAmount()).isEqualTo(testAmount);
            assertThat(result.getDescription()).isEqualTo(testDescription);
            assertThat(result.getCreatedAt()).isEqualTo(testCreatedAt);

            // Verify CategoryMapper was called
            categoryMapperMock.verify(() -> CategoryMapper.toResponseDTO(testCategory));
        }
    }

    @Test
    void toResponseDTO_WithNullValues_ShouldHandleNullsCorrectly() {
        testExpense.setExpenseDate(null);
        testExpense.setDescription(null);
        testExpense.setCreatedAt(null);

        try (MockedStatic<CategoryMapper> categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.toResponseDTO(testCategory))
                    .thenReturn(testCategoryResponseDTO);

            ExpenseResponseDTO result = ExpenseMapper.toResponseDTO(testExpense);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testExpenseId);
            assertThat(result.getUserId()).isEqualTo(testUserId);
            assertThat(result.getExpenseDate()).isNull();
            assertThat(result.getCategory()).isEqualTo(testCategoryResponseDTO);
            assertThat(result.getAmount()).isEqualTo(testAmount);
            assertThat(result.getDescription()).isNull();
            assertThat(result.getCreatedAt()).isNull();

            categoryMapperMock.verify(() -> CategoryMapper.toResponseDTO(testCategory));
        }
    }

    @Test
    void toResponseDTO_WithNullCategory_ShouldHandleNullCategory() {
        testExpense.setCategory(null);

        try (MockedStatic<CategoryMapper> categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.toResponseDTO(null))
                    .thenReturn(null);

            ExpenseResponseDTO result = ExpenseMapper.toResponseDTO(testExpense);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testExpenseId);
            assertThat(result.getUserId()).isEqualTo(testUserId);
            assertThat(result.getExpenseDate()).isEqualTo(testExpenseDate);
            assertThat(result.getCategory()).isNull();
            assertThat(result.getAmount()).isEqualTo(testAmount);
            assertThat(result.getDescription()).isEqualTo(testDescription);
            assertThat(result.getCreatedAt()).isEqualTo(testCreatedAt);

            categoryMapperMock.verify(() -> CategoryMapper.toResponseDTO(null));
        }
    }

    @Test
    void toResponseDTO_CategoryMapperIntegration_ShouldCallCategoryMapper() {
        CategoryResponseDTO expectedCategoryResponse = CategoryResponseDTO.builder()
                .id(testCategoryId)
                .name("Different Name")
                .description("Different Description")
                .build();

        try (MockedStatic<CategoryMapper> categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.toResponseDTO(testCategory))
                    .thenReturn(expectedCategoryResponse);

            ExpenseResponseDTO result = ExpenseMapper.toResponseDTO(testExpense);

            assertThat(result.getCategory()).isEqualTo(expectedCategoryResponse);
            assertThat(result.getCategory().getName()).isEqualTo("Different Name");
            assertThat(result.getCategory().getDescription()).isEqualTo("Different Description");

            categoryMapperMock.verify(() -> CategoryMapper.toResponseDTO(testCategory), times(1));
        }
    }

    // Edge cases and validation tests
    @Test
    void toEntity_WithZeroAmount_ShouldCreateEntityWithZeroAmount() {
        testExpenseDTO.setAmount(BigDecimal.ZERO);

        Expense result = ExpenseMapper.toEntity(testExpenseDTO, testCategory);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void toDTO_WithZeroAmount_ShouldCreateDTOWithZeroAmount() {
        testExpense.setAmount(BigDecimal.ZERO);

        ExpenseDTO result = ExpenseMapper.toDTO(testExpense);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void toResponseDTO_WithZeroAmount_ShouldCreateResponseDTOWithZeroAmount() {
        testExpense.setAmount(BigDecimal.ZERO);

        try (MockedStatic<CategoryMapper> categoryMapperMock = mockStatic(CategoryMapper.class)) {
            categoryMapperMock.when(() -> CategoryMapper.toResponseDTO(testCategory))
                    .thenReturn(testCategoryResponseDTO);

            ExpenseResponseDTO result = ExpenseMapper.toResponseDTO(testExpense);

            assertThat(result).isNotNull();
            assertThat(result.getAmount()).isEqualTo(BigDecimal.ZERO);
        }
    }
}