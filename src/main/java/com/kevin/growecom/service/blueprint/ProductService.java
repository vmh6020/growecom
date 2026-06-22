package com.kevin.growecom.service.blueprint;

import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.dto.product.UpdateProductRequest;

import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);

    ProductResponse update(Long id, UpdateProductRequest request);

    void deleteById(Long id);
}
