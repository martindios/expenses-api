package com.dios.expensesapi.mapper;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class CategoryMapperTest {

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
    void toEntity_WithValidData_ShouldCreateCategoryEntity() {
        // Act
        Category result = CategoryMapper.toEntity(testCategoryDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo(testCategoryDescription);

        // Verify that id is null (not set in DTO mapping)
        assertThat(result.getId()).isNull();
    }

    @Test
    void toEntity_WithNullDescription_ShouldCreateEntityWithNullDescription() {
        // Arrange
        testCategoryDTO.setDescription(null);

        // Act
        Category result = CategoryMapper.toEntity(testCategoryDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isNull();
        assertThat(result.getId()).isNull();
    }


    @Test
    void toEntity_WithEmptyDescription_ShouldCreateEntityWithEmptyDescription() {
        testCategoryDTO.setDescription("");

        Category result = CategoryMapper.toEntity(testCategoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo("");
        assertThat(result.getId()).isNull();
    }

    @Test
    void toEntity_WithLongDescription_ShouldCreateEntityWithLongDescription() {
        String longDescription = "A".repeat(500);
        testCategoryDTO.setDescription(longDescription);

        Category result = CategoryMapper.toEntity(testCategoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo(longDescription);
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDTO_WithValidCategory_ShouldCreateCategoryResponseDTO() {
        CategoryResponseDTO result = CategoryMapper.toResponseDTO(testCategory);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo(testCategoryDescription);
    }

    @Test
    void toResponseDTO_WithNullDescription_ShouldCreateResponseDTOWithNullDescription() {
        testCategory.setDescription(null);

        CategoryResponseDTO result = CategoryMapper.toResponseDTO(testCategory);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isNull();
    }

    @Test
    void toResponseDTO_WithEmptyDescription_ShouldCreateResponseDTOWithEmptyDescription() {
        testCategory.setDescription("");

        CategoryResponseDTO result = CategoryMapper.toResponseDTO(testCategory);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo(testCategoryName);
        assertThat(result.getDescription()).isEqualTo("");
    }

    // Edge cases and validation tests
    @Test
    void toEntity_WithMinimalData_ShouldCreateEntityWithOnlyName() {
        CategoryDTO minimalDTO = new CategoryDTO();
        minimalDTO.setName("Transport");
        minimalDTO.setDescription(null);

        Category result = CategoryMapper.toEntity(minimalDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Transport");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDTO_WithDifferentCategoryData_ShouldMapCorrectly() {
        Category entertainmentCategory = Category.builder()
                .id(UUID.randomUUID())
                .name("Entertainment")
                .description("Movies, games, and leisure activities")
                .build();

        CategoryResponseDTO result = CategoryMapper.toResponseDTO(entertainmentCategory);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entertainmentCategory.getId());
        assertThat(result.getName()).isEqualTo("Entertainment");
        assertThat(result.getDescription()).isEqualTo("Movies, games, and leisure activities");
    }

    @Test
    void toEntity_WithSpecialCharacters_ShouldHandleSpecialCharacters() {
        testCategoryDTO.setName("Food & Dining (Café)");
        testCategoryDTO.setDescription("Expenses for café visits, including coffee ☕ and pastries");

        Category result = CategoryMapper.toEntity(testCategoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Food & Dining (Café)");
        assertThat(result.getDescription()).isEqualTo("Expenses for café visits, including coffee ☕ and pastries");
    }

    @Test
    void toResponseDTO_WithSpecialCharacters_ShouldHandleSpecialCharacters() {
        testCategory.setName("Health & Medical 🏥");
        testCategory.setDescription("Medical expenses including doctor visits & medications");

        CategoryResponseDTO result = CategoryMapper.toResponseDTO(testCategory);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCategoryId);
        assertThat(result.getName()).isEqualTo("Health & Medical 🏥");
        assertThat(result.getDescription()).isEqualTo("Medical expenses including doctor visits & medications");
    }

}
