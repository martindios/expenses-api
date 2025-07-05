package com.example.jparestful.mapper;

import com.example.jparestful.dto.CategoryDTO;
import com.example.jparestful.model.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();
    }
}

