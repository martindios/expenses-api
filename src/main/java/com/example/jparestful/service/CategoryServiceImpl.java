package com.example.jparestful.service;

import com.example.jparestful.dto.CategoryDTO;
import com.example.jparestful.exception.DuplicateResourceException;
import com.example.jparestful.exception.ResourceNotFoundException;
import com.example.jparestful.mapper.CategoryMapper;
import com.example.jparestful.model.Category;
import com.example.jparestful.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category create(CategoryDTO categoryDTO) {
        if(categoryRepository.findByName(categoryDTO.getName()).isPresent()){
            throw new DuplicateResourceException("Category", "name",  categoryDTO.getName());
        }

        try {
            Category category = CategoryMapper.toEntity(categoryDTO);
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Category", "name",  categoryDTO.getName());
        }

    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UUID id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    categoryRepository.findByName(categoryDTO.getName())
                                    .filter(category -> !category.getId().equals(id))
                                            .ifPresent(category -> {
                                                throw new DuplicateResourceException("Category", "name",  categoryDTO.getName());
                                            });

                    existing.setName(categoryDTO.getName());
                    existing.setDescription(categoryDTO.getDescription());
                    return categoryRepository.save(existing);
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
