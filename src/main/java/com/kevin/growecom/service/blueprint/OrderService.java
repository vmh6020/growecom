package com.kevin.growecom.service.blueprint;

import com.kevin.growecom.dto.order.CreateOrderRequest;
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void save(Order order);
    OrderResponse findById(Long id);
    List<OrderResponse> findAll();
    void deleteById(Long id);
    OrderResponse createOrder(CreateOrderRequest request);
}
