package com.kevin.growecom.service.blueprint;

import com.kevin.growecom.dto.category.CategoryResponse;
import com.kevin.growecom.dto.category.CreateCategoryRequest;
import com.kevin.growecom.dto.category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request);

    List<CategoryResponse> findAll();

    CategoryResponse update(Long id, UpdateCategoryRequest request);

    CategoryResponse findById(Long id);

    void deleteById(Long id);
}

