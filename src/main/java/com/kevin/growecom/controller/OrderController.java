package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;
import com.kevin.growecom.dto.order.CreateOrderRequest;
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.model.Order;
import com.kevin.growecom.service.blueprint.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {
   private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders() {
        return ApiResponse.success("Get all orders successfully", orderService.findAll());
    }
    public ResponseEntity<ApiResponse<OrderResponse>> findOrderById(@RequestAttribute Long id) {
        return ApiResponse.success("Get order - " + id + "successfully", orderService.findById(id) );
    }
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
        return ApiResponse.created("Create order successfully", orderService.createOrder(request));
    }
}
