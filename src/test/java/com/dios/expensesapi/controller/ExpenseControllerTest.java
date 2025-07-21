package com.dios.expensesapi.controller;

import com.dios.expensesapi.config.JwtUtil;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.dto.ExpenseDTO;
import com.dios.expensesapi.dto.ExpenseResponseDTO;
import com.dios.expensesapi.service.CustomUserDetailsService;
import com.dios.expensesapi.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test configuration that disables security for controller testing
 */
@WebMvcTest(value = ExpenseController.class)
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
}
