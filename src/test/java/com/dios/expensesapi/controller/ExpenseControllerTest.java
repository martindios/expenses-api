package com.dios.expensesapi.controller;

import com.dios.expensesapi.config.JwtUtil;
import com.dios.expensesapi.config.SecurityConfig;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.service.CustomUserDetailsService;
import com.dios.expensesapi.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test configuration that disables security for controller testing
 */
@WebMvcTest(value = ExpenseController.class)
@Import(SecurityConfig.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite hacer peticiones HTTP mockeadas

    @MockitoBean
    private ExpenseService expenseService;

    // Se añade el JwtUtil y el CustorUserDetailsService para que se puedan cargar los beans
    // de configuración de seguridad correctamente
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // Convsersión entre JSON y objetos Java, sirve tanto para serializar como para deserializar
    @Autowired
    private ObjectMapper objectMapper;

    private ExpenseDTO testExpenseDTO;
    private ExpenseResponseDTO testExpenseResponseDTO;
    private final UUID testExpenseId = UUID.randomUUID();
    private final UUID testUserId = UUID.randomUUID();
    private final UUID testCategoryId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testExpenseDTO = new ExpenseDTO();
        testExpenseDTO.setExpenseDate(LocalDateTime.now());
        testExpenseDTO.setCategoryId(testCategoryId);
        testExpenseDTO.setAmount(new BigDecimal("100.50"));
        testExpenseDTO.setDescription("Test expense");

        CategoryResponseDTO categoryResponseDTO = CategoryResponseDTO.builder()
                .id(testCategoryId)
                .name("Food")
                .description("Food expenses")
                .build();

        testExpenseResponseDTO = ExpenseResponseDTO.builder()
                .id(testExpenseId)
                .userId(testUserId)
                .expenseDate(LocalDateTime.now())
                .category(categoryResponseDTO)
                .amount(new BigDecimal("100.50"))
                .description("Test expense")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    void findAll_ShouldReturnAllExpenses() throws Exception {
        // Arrange
        List<ExpenseResponseDTO> expenses = Arrays.asList(testExpenseResponseDTO);
        when(expenseService.findAll()).thenReturn(expenses);

        // Act and assert
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray()) // Valida que la raíz sea un array
                .andExpect(jsonPath("$[0].id").value(testExpenseId.toString())) /* Valida que el
         primer elemento del array coincida con id */
                .andExpect(jsonPath("$[0].amount").value(100.50))
                .andExpect(jsonPath("$[0].description").value("Test expense"))
                .andExpect(jsonPath("$[0].category.name").value("Food"))
                .andDo(print()); // Imprime por consola la petición y la respuesta

        verify(expenseService).findAll();
    }

    @Test
    @WithMockUser
    void findById_WithValidId_ShouldReturnExpense() throws Exception {
        // Arrange
        when(expenseService.findById(testExpenseId)).thenReturn(Optional.of(testExpenseResponseDTO));

        // Act and assert
        mockMvc.perform(get("/api/expenses/{id}", testExpenseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testExpenseId.toString()))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.description").value("Test expense"))
                .andExpect(jsonPath("$.category.name").value("Food"))
                .andDo(print());

        verify(expenseService).findById(testExpenseId);
    }

    @Test
    @WithMockUser
    void findById_WithNonExistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(expenseService.findById(testExpenseId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/{id}", testExpenseId))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(expenseService).findById(testExpenseId);
    }

    @Test
    @WithMockUser
    void create_WithValidData_ShouldCreateExpense() throws Exception {
        when(expenseService.create(any(ExpenseDTO.class))).thenReturn(testExpenseResponseDTO);

        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testExpenseId.toString()))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.description").value("Test expense"))
                .andExpect(jsonPath("$.category.name").value("Food"))
                .andDo(print());

        verify(expenseService).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        ExpenseDTO invalidExpenseDTO = new ExpenseDTO();
        // Missing required fields to trigger validation errors

        // Act & Assert
        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithNegativeAmount_ShouldReturnBadRequest() throws Exception {
        testExpenseDTO.setAmount(new BigDecimal("-50.00"));

        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // Nunca se llama a .create() de expenseService porque no se puede settear el amount a negativo por el ExpenseDTO
        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithNullExpenseDate_ShouldReturnBadRequest() throws Exception {
        testExpenseDTO.setExpenseDate(null);

        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithInvalidCategory_ShouldReturnBadRequest() throws Exception {
        testExpenseDTO.setCategoryId(null);
        testExpenseDTO.setCategoryName(null);

        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithValidData_ShouldUpdateExpense() throws Exception {
        // Hay que poner el eq ya que usamos luego el any en la misma llamada
        when(expenseService.update(eq(testExpenseId), any(ExpenseDTO.class))).thenReturn(testExpenseResponseDTO);

        mockMvc.perform(put("/api/expenses/{id}", testExpenseId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testExpenseId.toString()))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.description").value("Test expense"))
                .andDo(print());

        verify(expenseService).update(eq(testExpenseId), any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithNonExistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(expenseService.update(eq(testExpenseId), any(ExpenseDTO.class)))
                .thenThrow(new ResourceNotFoundException("Expense", testExpenseId.toString()));

        mockMvc.perform(put("/api/expenses/{id}", testExpenseId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(expenseService).update(eq(testExpenseId), any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ExpenseDTO invalidExpenseDTO = new ExpenseDTO();
        invalidExpenseDTO.setAmount(new BigDecimal("-100.00")); // Invalid negative amount

        mockMvc.perform(put("/api/expenses/{id}", testExpenseId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(expenseService, never()).update(any(UUID.class), any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void delete_WithValidId_ShouldDeleteExpense() throws Exception {
        doNothing().when(expenseService).deleteById(testExpenseId);

        mockMvc.perform(delete("/api/expenses/{id}", testExpenseId)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(expenseService).deleteById(testExpenseId);
    }

    @Test
    @WithMockUser
    void delete_WithNonExistentId_ShouldThrowResourceNotFoundException() throws Exception {
        // doThrow is used in void methods, lets you stub a void method without calling it
        doThrow(new ResourceNotFoundException("Expense", testExpenseId.toString()))
                .when(expenseService).deleteById(testExpenseId);

        mockMvc.perform(delete("/api/expenses/{id}", testExpenseId)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(expenseService).deleteById(testExpenseId);
    }

    @Test
    void findAll_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isUnauthorized())
                .andDo(print());

        verify(expenseService, never()).findAll();
    }

    @Test
    void create_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isUnauthorized())
                .andDo(print());

        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithLongDescription_ShouldReturnBadRequest() throws Exception {
        String longDescription = "A".repeat(1001); // Exceeds 1000-character limit
        testExpenseDTO.setDescription(longDescription);

        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithAmountExceedingPrecision_ShouldReturnBadRequest() throws Exception {
        testExpenseDTO.setAmount(new BigDecimal("123456789.123")); // More than 2 decimal places

        mockMvc.perform(post("/api/expenses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testExpenseDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(expenseService, never()).create(any(ExpenseDTO.class));
    }

}
