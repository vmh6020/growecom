package com.kevin.growecom.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
