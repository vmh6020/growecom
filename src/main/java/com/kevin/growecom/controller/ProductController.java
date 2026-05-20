package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;
import com.kevin.growecom.dto.PaginationResponse;
import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.service.blueprint.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody CreateProductRequest request) {
        return ApiResponse.success(
                "Create product successfully",
                productService.createProduct(request)
        );
    }
//    @GetMapping("/products")
//    public ResponseEntity<ApiResponse<PaginationResponse<ProductResponse>>> findAll(
//            @ParameterObject
//            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
//            Pageable userPageable
//    ) {
//        return ApiResponse.success(
//                "Retrieve All Product Successfully",
//                productService.findAll(userPageable));
//    }

}
