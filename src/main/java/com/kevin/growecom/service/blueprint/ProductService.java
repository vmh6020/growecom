package com.kevin.growecom.service.blueprint;

import com.kevin.growecom.dto.PaginationResponse;
import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);
//    PaginationResponse<ProductResponse> findAll(Pageable pageable);
}
