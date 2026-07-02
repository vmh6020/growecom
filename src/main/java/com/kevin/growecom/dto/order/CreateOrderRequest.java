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

    @NotEmpty(message = "Cart items cannot be empty")
    private List<CartItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemRequest {
        @NotNull(message = "Product id cannot be null")
        private Long productId;
        @NotNull(message = "Product quantity cannot be null")
        private Integer quantity;
    }
}