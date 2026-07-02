package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;

import com.kevin.growecom.dto.order.UpdateOrderRequest;
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders() {
        return ApiResponse.ok("Get all orders successfully", orderService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        return ApiResponse.ok("Get order - " + id + " successfully", orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder() {
        return ApiResponse.created("Create order successfully", orderService.create());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(@Valid @RequestBody UpdateOrderRequest request) {
        return ApiResponse.ok("Update order successfully", orderService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ApiResponse.ok("Delete order successfully", null);
    }
}
