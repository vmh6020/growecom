package com.kevin.growecom.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequest {
    @NotNull(message = "Order id cannot be null")
    private Long id;
    @NotNull(message = "User cannot be null")
    private Long userId;
    @NotEmpty(message = "Order items cannot be empty")
    private List<UpdateOrderItemRequest> orderItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateOrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
    // status -> sau khi mua hang // totalPrice -> tinh tien dua tren orderItems moi
    // user -> khong thay doi
}
