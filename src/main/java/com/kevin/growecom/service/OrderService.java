package com.kevin.growecom.service;

import com.kevin.growecom.dto.CreateOrderRequest;
import com.kevin.growecom.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    void deleteById(Long id);
    Order createOrder(CreateOrderRequest request);


}
