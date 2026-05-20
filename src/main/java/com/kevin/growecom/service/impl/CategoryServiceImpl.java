package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.category.CategoryResponse;
import com.kevin.growecom.dto.category.CreateCategoryRequest;
import com.kevin.growecom.dto.category.UpdateCategoryRequest;
import com.kevin.growecom.model.Category;
import com.kevin.growecom.repository.CategoryRepository;
import com.kevin.growecom.service.blueprint.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            // BusinessRuleException (RuntimeException temporary)
            throw new RuntimeException("Category name" + request.getName() + "already existed");
        }
        Category newCategory = Category.builder().name(request.getName()).description(request.getDescription()).build();

        Category savedCategory = categoryRepository.save(newCategory);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();
    }

    @Override
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
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( "Product - " + id + " not found"));

        // nguoi dung nhap ten ?
        String newName = request.getName();
        if (newName != null && !newName.trim().isEmpty()) {
            if (categoryRepository.existsByName(newName) && !newName.equals(category.getName())) {
                throw new RuntimeException("Name already existed");
            }
            category.setName(newName);
        }
        String newDescription = request.getDescription();
        if (newDescription != null) {
            category.setDescription(newDescription);
        }
        // yes -> check trung voi ten cu ? -> yes : set name (bang name cu, van giu nguyen)
                                        // -> no : -> check ten xuat hien db ? -> yes: throw exception
                                        //                                      -> no : setName (bang name moi)
        // nguoi dung nhap description ? -> yes: set description
                                            // -> no ->
        // save category (ke ca set hay khong set)
        //request.getDescription() !=
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category " + id + " not found"));
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find product - " + id));
        categoryRepository.delete(category);
    }

}
