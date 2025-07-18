package com.dios.expensesapi.service;

import com.dios.expensesapi.dto.CategoryDTO;
import com.dios.expensesapi.dto.CategoryResponseDTO;
import com.dios.expensesapi.exception.DuplicateResourceException;
import com.dios.expensesapi.exception.ResourceNotFoundException;
import com.dios.expensesapi.mapper.CategoryMapper;
import com.dios.expensesapi.mapper.ExpenseMapper;
import com.dios.expensesapi.model.Category;
import com.dios.expensesapi.model.User;
import com.dios.expensesapi.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<CategoryResponseDTO> findAll() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(CategoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Optional<CategoryResponseDTO> findById(UUID id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toResponseDTO);
    }

    @Override
    public CategoryResponseDTO create(CategoryDTO categoryDTO) {
        if(categoryRepository.findByName(categoryDTO.getName()).isPresent()){
            throw new DuplicateResourceException("Category", "name",  categoryDTO.getName());
        }

        try {
            Category category = CategoryMapper.toEntity(categoryDTO);
            Category saved = categoryRepository.save(category);
            return CategoryMapper.toResponseDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Category", "name",  categoryDTO.getName());
        }

    }

    @Override
    public CategoryResponseDTO update(UUID id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    categoryRepository.findByName(categoryDTO.getName())
                                    .filter(category -> !category.getId().equals(id))
                                            .ifPresent(category -> {
                                                throw new DuplicateResourceException("Category", "name",  categoryDTO.getName());
                                            });

                    existing.setName(categoryDTO.getName());
                    existing.setDescription(categoryDTO.getDescription());
                    Category updated = categoryRepository.save(existing);
                    return  CategoryMapper.toResponseDTO(updated);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category", id.toString()));
    }


    @Override
    public void deleteById(UUID id) {
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Category", id.toString());
        }
        categoryRepository.deleteById(id);
    }
}
