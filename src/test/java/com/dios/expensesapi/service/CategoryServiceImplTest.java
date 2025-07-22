package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.exception.DuplicateResourceException;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryDTO testCategoryDTO;
    private final UUID testCategoryId = UUID.randomUUID();
    private final String testCategoryName = "Food & Dining";
    private final String testCategoryDescription = "Expenses related to meals, restaurants, and food purchases";

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(testCategoryId)
                .name(testCategoryName)
                .description(testCategoryDescription)
                .build();

        testCategoryDTO = new CategoryDTO();
        testCategoryDTO.setName(testCategoryName);
        testCategoryDTO.setDescription(testCategoryDescription);
    }

    @Test
    void findAll_ShouldReturnAllCategories() {
        // Arrange
        Category category2 = Category.builder()
                .id(UUID.randomUUID())
                .name("Transportation")
                .description("Transport expenses")
                .build();

        List<Category> categories = Arrays.asList(testCategory, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        Iterable<CategoryResponseDTO> result = categoryService.findAll();

        // Assert
        assertThat(result).isNotNull();
        List<CategoryResponseDTO> resultList = (List<CategoryResponseDTO>) result;
        assertThat(resultList).hasSize(2);

        CategoryResponseDTO responseDTO = resultList.getFirst();
        assertThat(responseDTO.getId()).isEqualTo(testCategoryId);
        assertThat(responseDTO.getName()).isEqualTo(testCategoryName);
        assertThat(responseDTO.getDescription()).isEqualTo(testCategoryDescription);

        verify(categoryRepository).findAll();
    }

    @Test
    void findById_WhenCategoryExists_ShouldReturnCategoryResponseDTO() {
        // Arrange
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));

        // Act
        Optional<CategoryResponseDTO> result = categoryService.findById(testCategoryId);

        // Assert
        assertThat(result).isPresent();
        CategoryResponseDTO responseDTO = result.get();
        assertThat(responseDTO.getId()).isEqualTo(testCategoryId);
        assertThat(responseDTO.getName()).isEqualTo(testCategoryName);
        assertThat(responseDTO.getDescription()).isEqualTo(testCategoryDescription);

        verify(categoryRepository).findById(testCategoryId);
    }

    @Test
    void findById_WhenCategoryDoesNotExists_ShouldReturnEmptyOptional() {
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.empty());

        Optional<CategoryResponseDTO> result = categoryService.findById(testCategoryId);

        assertThat(result).isEmpty();
        verify(categoryRepository).findById(testCategoryId);
    }

    @Test
    void create_WithValidData_ShouldCreateCategory() {
        when(categoryRepository.findByName(testCategoryName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryResponseDTO result = categoryService.create(testCategoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo(testCategoryDescription);

        verify(categoryRepository).findByName(testCategoryName);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void create_WithDuplicateName_ShouldThrowDuplicateResourceException() {
        // Arrange
        when(categoryRepository.findByName(testCategoryName)).thenReturn(Optional.of(testCategory));

        // Act & Assert
        assertThatThrownBy(() -> categoryService.create(testCategoryDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining("name")
                .hasMessageContaining(testCategoryName);

        verify(categoryRepository).findByName(testCategoryName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void create_WithDataIntegrityViolation_ShouldThrowDuplicateResourceException() {
        when(categoryRepository.findByName(testCategoryName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenThrow(new DataIntegrityViolationException("Duplicate"));

        assertThatThrownBy(() -> categoryService.create(testCategoryDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining("name")
                .hasMessageContaining(testCategoryName);

        verify(categoryRepository).findByName(testCategoryName);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void update_WithValidData_ShouldUpdateCategory() {
        // Arrange
        String updatedName = "Updated Food";
        String updatedDescription = "Updated description";

        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName(updatedName);
        updateDTO.setDescription(updatedDescription);

        Category updatedCategory = Category.builder()
                .id(testCategoryId)
                .name(updatedName)
                .description(updatedDescription)
                .build();

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.findByName(updatedName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        CategoryResponseDTO result = categoryService.update(testCategoryId, updateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo(updatedName);
        assertThat(result.getDescription()).isEqualTo(updatedDescription);

        verify(categoryRepository).findById(testCategoryId);
        verify(categoryRepository).findByName(updatedName);
        verify(categoryRepository).save(testCategory);
    }

    @Test
    void update_WithNonExistentCategory_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(testCategoryId, testCategoryDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining(testCategoryId.toString());

        verify(categoryRepository).findById(testCategoryId);
        verify(categoryRepository, never()).findByName(any());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void update_WithDuplicateName_ShouldThrowDuplicateResourceException() {
        UUID differentCategoryId = UUID.randomUUID();
        Category existingCategoryWithSameName = Category.builder()
                .id(differentCategoryId)
                .name(testCategoryName)
                .description("Different description")
                .build();

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.findByName(testCategoryName)).thenReturn(Optional.of(existingCategoryWithSameName));

        assertThatThrownBy(() -> categoryService.update(testCategoryId, testCategoryDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining("name")
                .hasMessageContaining(testCategoryName);

        verify(categoryRepository).findById(testCategoryId);
        verify(categoryRepository).findByName(testCategoryName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void update_WithSameNameForSameCategory_ShouldUpdateSuccessfully() {
        // Arrange - Actualizamos la misma categoría con el mismo nombre pero descripción diferente
        String updatedDescription = "Updated description";
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName(testCategoryName); // Mismo nombre
        updateDTO.setDescription(updatedDescription);

        Category updatedCategory = Category.builder()
                .id(testCategoryId)
                .name(testCategoryName)
                .description(updatedDescription)
                .build();

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.findByName(testCategoryName)).thenReturn(Optional.of(testCategory)); // Mismo ID
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        CategoryResponseDTO result = categoryService.update(testCategoryId, updateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo(updatedDescription);

        verify(categoryRepository).findById(testCategoryId);
        verify(categoryRepository).findByName(testCategoryName);
        verify(categoryRepository).save(testCategory);
    }

    @Test
    void deleteById_WithValidId_ShouldDeleteCategory() {
        when(categoryRepository.existsById(testCategoryId)).thenReturn(true);

        categoryService.deleteById(testCategoryId);

        verify(categoryRepository).existsById(testCategoryId);
        verify(categoryRepository).deleteById(testCategoryId);
    }

    @Test
    void deleteById_WithNonExistentId_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.existsById(testCategoryId)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.deleteById(testCategoryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining(testCategoryId.toString());

        verify(categoryRepository).existsById(testCategoryId);
        verify(categoryRepository, never()).deleteById(any());
    }
}
