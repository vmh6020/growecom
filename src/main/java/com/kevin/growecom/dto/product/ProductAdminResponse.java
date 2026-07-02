package com.kevin.growecom.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductAdminResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer totalStock;
    private List<WarehouseStockDTO> warehouseStocks;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WarehouseStockDTO {
        private int inventoryId;
        private Long warehouseId;
        private Long waehouseName;
        private String location;
        private Long quantity;
    }
}