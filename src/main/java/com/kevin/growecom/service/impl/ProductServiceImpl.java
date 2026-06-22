package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.product.CreateProductRequest;
import com.kevin.growecom.dto.product.ProductAdminResponse;
import com.kevin.growecom.dto.product.ProductResponse;
import com.kevin.growecom.dto.product.UpdateProductRequest;
import com.kevin.growecom.model.Category;
import com.kevin.growecom.model.Inventory;
import com.kevin.growecom.model.Product;
import com.kevin.growecom.model.Warehouse;
import com.kevin.growecom.repository.CategoryRepository;
import com.kevin.growecom.repository.InventoryRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.repository.WarehouseRepository;
import com.kevin.growecom.service.blueprint.ProductService;
import com.kevin.growecom.service.blueprint.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Long categoryId = request.getCategoryId();
        Category  category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category - " + categoryId + " does not exist"));

        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(category)
                .build();
        List<Long> warehouseIds = request.getStocks().stream().map(CreateProductRequest.ProductStockRequest::getWarehouseId).toList();
        List<Warehouse> warehouses = warehouseRepository.findAllById(warehouseIds);
        Map<Long, Warehouse> warehouseIdMap = warehouses.stream()
                .collect(Collectors.toMap(
                        Warehouse::getId,
                        w -> w));
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
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product - " + id + " does not exists"));
        return toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product reqProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product - " + id + " does not exist"));
        if (request.getCategoryId() != null) {
            Long categoryId = request.getCategoryId();
            if (!reqProduct.getCategory().getId().equals(categoryId)) {
                Category reqCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category - " + categoryId + " does not exist"));
                reqProduct.setCategory(reqCategory);
            }
        }
        if (request.getName() != null) reqProduct.setName(request.getName());
        if (request.getPrice() != null) reqProduct.setPrice(request.getPrice());

        List<Inventory> reqProductInventories = reqProduct.getInventories();
        List<Long> productWarehouseIds = reqProductInventories.stream().map(i -> i.getWarehouse().getId()).toList();

        Map<Long, Inventory> warehouseInventoriesMap = reqProductInventories.stream().collect(Collectors.toMap(i -> i.getWarehouse().getId(), i -> i));
        List<UpdateProductRequest.ProductStockRequest> stocks = request.getStocks();
        if (stocks != null) {
            for (var s : stocks) {
                Long reqWarehouseId = s.getWarehouseId();
                if (productWarehouseIds.contains(reqWarehouseId)) {
                    Inventory inventoryToSave = warehouseInventoriesMap.get(reqWarehouseId);
                    inventoryToSave.setQuantity(s.getQuantity());
                } else {
                    Warehouse newWarehouse = warehouseRepository.findById(reqWarehouseId)
                            .orElseThrow(() -> new RuntimeException("Warehouse - " + reqWarehouseId + " does not exist"));
                    Inventory inventoryToSave  = Inventory.builder()
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
//    @Override
//    public PaginationResponse<ProductAdminResponse> findAll(Pageable pageable) {
//        List<Product> products = productRepository.findAll();
//
//    }


}
