package com.dios.expensesapi.controller;

import com.dios.expensesapi.config.JwtUtil;
import com.dios.expensesapi.config.SecurityConfig;
import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.service.CategoryService;
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

@WebMvcTest(value = CategoryController.class)
@Import(SecurityConfig.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO testCategoryDTO;
    private CategoryResponseDTO testCategoryResponseDTO;
    private final UUID testCategoryId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testCategoryDTO = new CategoryDTO();
        testCategoryDTO.setName("Food & Dining");
        testCategoryDTO.setDescription("Expenses related to meals, restaurants, and food purchases");

        testCategoryResponseDTO = CategoryResponseDTO.builder()
                .id(testCategoryId)
                .name("Food & Dining")
                .description("Expenses related to meals, restaurants, and food purchases")
                .build();
    }

    @Test
    @WithMockUser
    void findAll_ShouldReturnAllCategories() throws Exception {
        // Arrange
        List<CategoryResponseDTO> categories = Arrays.asList(testCategoryResponseDTO);
        when(categoryService.findAll()).thenReturn(categories);

        // Act and assert
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testCategoryId.toString()))
                .andExpect(jsonPath("$[0].name").value("Food & Dining"))
                .andExpect(jsonPath("$[0].description").value("Expenses related to meals, restaurants, and food purchases"))
                .andDo(print());

        verify(categoryService).findAll();
    }

    @Test
    @WithMockUser
    void findById_WithValidId_ShouldReturnCategory() throws Exception {
        when(categoryService.findById(testCategoryId)).thenReturn(Optional.of(testCategoryResponseDTO));

        mockMvc.perform(get("/api/categories/{id}", testCategoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCategoryId.toString()))
                .andExpect(jsonPath("$.name").value("Food & Dining"))
                .andExpect(jsonPath("$.description").value("Expenses related to meals, restaurants, and food purchases"))
                .andDo(print());

        verify(categoryService).findById(testCategoryId);
    }

    @Test
    @WithMockUser
    void findById_WithNonExistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(categoryService.findById(testCategoryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/{id}", testCategoryId))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(categoryService).findById(testCategoryId);
    }

    @Test
    @WithMockUser
    void create_WithValidData_ShouldCreateCategory() throws Exception {
        when(categoryService.create(any(CategoryDTO.class))).thenReturn(testCategoryResponseDTO);

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCategoryId.toString()))
                .andExpect(jsonPath("$.name").value("Food & Dining"))
                .andExpect(jsonPath("$.description").value("Expenses related to meals, restaurants, and food purchases"))
                .andDo(print());

        verify(categoryService).create(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CategoryDTO invalidCategoryDTO = new CategoryDTO();

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).create(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithBlankName_ShouldReturnBadRequest() throws Exception {
        testCategoryDTO.setName("");

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).create(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithNullName_ShouldReturnBadRequest() throws Exception {
        testCategoryDTO.setName(null);

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).create(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void create_WithLongDescription_ShouldReturnBadRequest() throws Exception {
        String longDescription = "A".repeat(1001);
        testCategoryDTO.setDescription(longDescription);

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).create(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithValidData_ShouldUpdateCategory() throws Exception {
        when(categoryService.update(eq(testCategoryId), any(CategoryDTO.class))).thenReturn(testCategoryResponseDTO);

        mockMvc.perform(put("/api/categories/{id}", testCategoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCategoryId.toString()))
                .andExpect(jsonPath("$.name").value("Food & Dining"))
                .andExpect(jsonPath("$.description").value("Expenses related to meals, restaurants, and food purchases"))
                .andDo(print());

        verify(categoryService).update(eq(testCategoryId), any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithNonExistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(categoryService.update(eq(testCategoryId), any(CategoryDTO.class)))
                .thenThrow(new ResourceNotFoundException("Category", testCategoryId.toString()));

        mockMvc.perform(put("/api/categories/{id}", testCategoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(categoryService).update(eq(testCategoryId), any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CategoryDTO invalidCategoryDTO = new CategoryDTO();
        invalidCategoryDTO.setName("");

        mockMvc.perform(put("/api/categories/{id}", testCategoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).update(any(UUID.class), any(CategoryDTO.class));
    }


    @Test
    @WithMockUser
    void delete_WithValidId_ShouldDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteById(testCategoryId);

        mockMvc.perform(delete("/api/categories/{id}", testCategoryId)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(categoryService).deleteById(testCategoryId);
    }

    @Test
    @WithMockUser
    void delete_WithNonExistentId_ShouldThrowResourceNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Category", testCategoryId.toString()))
                .when(categoryService).deleteById(testCategoryId);

        mockMvc.perform(delete("/api/categories/{id}", testCategoryId)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(categoryService).deleteById(testCategoryId);
    }

    @Test
    void findAll_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized())
                .andDo(print());

        verify(categoryService, never()).findAll();
    }

    @Test
    void create_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategoryDTO)))
                .andExpect(status().isUnauthorized())
                .andDo(print());

        verify(categoryService, never()).create(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithNullName_ShouldReturnBadRequest() throws Exception {
        CategoryDTO invalidCategoryDTO = new CategoryDTO();
        invalidCategoryDTO.setName(null);
        invalidCategoryDTO.setDescription("Valid description");

        mockMvc.perform(put("/api/categories/{id}", testCategoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).update(any(UUID.class), any(CategoryDTO.class));
    }

    @Test
    @WithMockUser
    void update_WithLongDescription_ShouldReturnBadRequest() throws Exception {
        CategoryDTO invalidCategoryDTO = new CategoryDTO();
        invalidCategoryDTO.setName("Valid name");
        String longDescription = "A".repeat(1001);
        invalidCategoryDTO.setDescription(longDescription);

        mockMvc.perform(put("/api/categories/{id}", testCategoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(categoryService, never()).update(any(UUID.class), any(CategoryDTO.class));
    }
}
