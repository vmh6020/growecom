package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.PaginationResponse;
import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.dto.product.UpdateProductRequest;
import com.kevin.growecom.exception.BaseException;
import com.kevin.growecom.model.Category;
import com.kevin.growecom.model.Inventory;
import com.kevin.growecom.model.Product;
import com.kevin.growecom.model.Warehouse;
import com.kevin.growecom.repository.CategoryRepository;
import com.kevin.growecom.repository.InventoryRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.repository.WarehouseRepository;
import com.kevin.growecom.repository.specification.ProductSpecification;
import com.kevin.growecom.service.ProductService;
import com.kevin.growecom.util.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kevin.growecom.dto.PaginationResponse.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Long categoryId = request.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));

        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(category)
                .build();
        List<Long> warehouseIds = request.getStocks().stream().map(CreateProductRequest.ProductStockRequest::getWarehouseId).toList();
        List<Warehouse> warehouses = warehouseRepository.findAllById(warehouseIds);
        Map<Long, Warehouse> warehouseIdMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getId, w -> w));
        List<Inventory> inventories = new ArrayList<>();
        for (var stock : request.getStocks()) {
            Warehouse warehouse = warehouseIdMap.get(stock.getWarehouseId());
            Inventory newInventory = Inventory.builder()
                    .product(newProduct)
                    .quantity(stock.getQuantity())
                    .warehouse(warehouse)
                    .build();
            inventories.add(newInventory);
        }
        newProduct.setInventories(inventories);
        Product savedProduct = productRepository.save(newProduct);
        return toResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductServiceImpl::toResponse).toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
        return toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product reqProduct = productRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
        if (request.getCategoryId() != null) {
            Long categoryId = request.getCategoryId();
            if (!reqProduct.getCategory().getId().equals(categoryId)) {
                Category reqCategory = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
                reqProduct.setCategory(reqCategory);
            }
        }
        if (request.getName() != null) reqProduct.setName(request.getName());
        if (request.getPrice() != null) reqProduct.setPrice(request.getPrice());

        List<Inventory> reqProductInventories = reqProduct.getInventories();
        List<Long> productWarehouseIds = reqProductInventories.stream().map(i -> i.getWarehouse().getId()).toList();
        Map<Long, Inventory> warehouseInventoriesMap = reqProductInventories.stream()
                .collect(Collectors.toMap(i -> i.getWarehouse().getId(), i -> i));
        List<UpdateProductRequest.ProductStockRequest> stocks = request.getStocks();
        if (stocks != null) {
            for (var s : stocks) {
                Long reqWarehouseId = s.getWarehouseId();
                if (productWarehouseIds.contains(reqWarehouseId)) {
                    Inventory inventoryToSave = warehouseInventoriesMap.get(reqWarehouseId);
                    inventoryToSave.setQuantity(s.getQuantity());
                } else {
                    Warehouse newWarehouse = warehouseRepository.findById(reqWarehouseId)
                            .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
                    Inventory inventoryToSave = Inventory.builder()
                            .product(reqProduct)
                            .warehouse(newWarehouse)
                            .quantity(s.getQuantity())
                            .build();
                    reqProductInventories.add(inventoryToSave);
                }
            }
        }
        reqProduct.setInventories(reqProductInventories);
        Product savedProduct = productRepository.save(reqProduct);
        return toResponse(savedProduct);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private static ProductResponse toResponse(Product product) {
        int totalStock = product.getInventories().stream()
                .mapToInt(Inventory::getQuantity)
                .sum();
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .totalStock(totalStock)
                .build();
    }
    @Override
    public PaginationResponse<ProductResponse> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.filterProducts(keyword, categoryId, minPrice, maxPrice);
        Page<Product> pageResult = productRepository.findAll(spec, pageable);

        List<ProductResponse> productResponses = pageResult.getContent().stream()
                .map(ProductServiceImpl::toResponse)
                .toList();
                
        MetaDTO meta = MetaDTO.builder()
                .page(pageResult.getNumber() + 1)
                .pageSize(pageResult.getSize())
                .pages(pageResult.getTotalPages())
                .total(pageResult.getTotalElements())
                .build();
                
        return PaginationResponse.<ProductResponse>builder()
                .result(productResponses)
                .meta(meta)
                .build();
    }
}
