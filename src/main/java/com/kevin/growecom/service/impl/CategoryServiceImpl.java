package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.category.CategoryResponse;
import com.kevin.growecom.dto.category.CreateCategoryRequest;
import com.kevin.growecom.dto.category.UpdateCategoryRequest;
import com.kevin.growecom.exception.BaseException;
import com.kevin.growecom.model.Category;
import com.kevin.growecom.repository.CategoryRepository;
import com.kevin.growecom.service.CategoryService;
import com.kevin.growecom.util.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse create(CreateCategoryRequest request) {
        String newName = request.getName();
        if (categoryRepository.existsByName(newName)) {
            throw new BaseException(ErrorCode.NAME_ALREADY_EXISTS);
        }
        Category newCategory = Category.builder().name(newName).description(request.getDescription()).build();
        Category savedCategory = categoryRepository.save(newCategory);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();
    }

    @Override
    @Cacheable(value = "categories")
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .build())
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));

        String newName = request.getName();
        if (newName != null && !newName.trim().isEmpty()) {
            if (categoryRepository.existsByName(newName) && !newName.equals(category.getName())) {
                throw new BaseException(ErrorCode.NAME_ALREADY_EXISTS);
            }
            category.setName(newName);
        }
        String newDescription = request.getDescription();
        if (newDescription != null) {
            category.setDescription(newDescription);
        }
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
        categoryRepository.delete(category);
    }
}
