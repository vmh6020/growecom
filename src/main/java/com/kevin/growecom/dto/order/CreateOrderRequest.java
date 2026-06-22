package com.kevin.growecom.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotNull(message = "User id cannot be null")
    private Long userId;
    @NotEmpty(message = "order items cannot be empty")
    private List<OrderItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemRequest {
        @NotNull(message = "Product id cannot be null")
        private Long productId;
        @NotNull(message = "Product quantity cannot be null")
        private Integer quantity;
    }
}