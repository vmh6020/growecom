package com.kevin.growecom.service;

import com.kevin.growecom.dto.order.CreateOrderRequest;
import com.kevin.growecom.dto.order.UpdateOrderRequest; // Import thêm Request sửa
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.model.Order;

import java.util.List;

public interface OrderService {
    void save(Order order);
    OrderResponse findById(Long id);
    List<OrderResponse> findAll();
    void deleteById(Long id);
    OrderResponse create();
    OrderResponse update(UpdateOrderRequest request);
}
