package com.kevin.growecom.service;

import com.kevin.growecom.dto.PaginationResponse;
import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.dto.product.UpdateProductRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse update(Long id, UpdateProductRequest request);

    void deleteById(Long id);

    PaginationResponse<ProductResponse> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
