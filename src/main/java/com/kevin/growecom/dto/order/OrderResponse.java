package com.kevin.growecom.dto.order;

import com.kevin.growecom.util.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Long userId;
    private List<OrderItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        private Long id;
        private Integer quantity;
        private BigDecimal price;
        private Long productId;
        private String productName;
    }
}
