package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.PaginationResponse;
import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.model.Inventory;
import com.kevin.growecom.model.Product;
import com.kevin.growecom.repository.InventoryRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.service.blueprint.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        productRepository.save(newProduct);
        Inventory newInventory = Inventory.builder().product(newProduct)
                .quantity(request.getQuantity())
                .build();
        inventoryRepository.save(newInventory);
        return ProductResponse.builder()
                .id(newProduct.getId())
                .name(newProduct.getName())
                .price(newProduct.getPrice())
                .initialStock(newInventory.getQuantity())
                .build();
    }

//    @Override
//    public PaginationResponse<ProductResponse> findAll(Pageable pageable) {
//        List<Product> products = productRepository.findAll();
//
//    }


}
