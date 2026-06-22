package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;
import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.dto.product.UpdateProductRequest;
import com.kevin.growecom.service.blueprint.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts () {
        return ApiResponse.success("Get all products successfully", productService.findAll());
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById (@PathVariable Long id) {
        return ApiResponse.success("Get all products successfully", productService.findById(id));
    }
    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody CreateProductRequest request) {
        return ApiResponse.success("Create product successfully", productService.create(request));
    }
    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ApiResponse.success("Update product successfully", productService.update(id, request));
    }
    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ApiResponse.success("Update product successfully", null);
    }

}
