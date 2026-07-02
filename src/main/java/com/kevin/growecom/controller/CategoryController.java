package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;
import com.kevin.growecom.dto.category.CategoryResponse;
import com.kevin.growecom.dto.category.CreateCategoryRequest;
import com.kevin.growecom.dto.category.UpdateCategoryRequest;
import com.kevin.growecom.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        return ApiResponse.ok("Retrieved all categories successfully", categoryService.findAll());
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        return ApiResponse.ok("Retrieve category " + id + " successfully", categoryService.findById(id));
    }
    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return ApiResponse.created("Category created successfully!", categoryService.create(request));
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        return ApiResponse.ok("Update category successfully", categoryService.update(id, request));
    }
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ApiResponse.ok("Delete category successfully", null);
    }
}
