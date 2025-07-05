package com.dios.expensesapi.mapper;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.model.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();
    }
}

