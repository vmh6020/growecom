package com.kevin.growecom.controller;

import com.kevin.growecom.dto.CreateOrderRequest;
import com.kevin.growecom.entity.Order;
import com.kevin.growecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
//        return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(request));
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
