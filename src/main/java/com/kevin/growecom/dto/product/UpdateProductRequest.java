package com.kevin.growecom.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    private String name;
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private BigDecimal price;
    private Long categoryId;
    private List<ProductStockRequest> stocks;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductStockRequest {
        @NotNull(message = "Warehouse id cannot be null")
        private Long warehouseId;
        @Min(value = 0, message = "Quantity must be greater than or equal to zero")
        private Integer quantity;
    }
}