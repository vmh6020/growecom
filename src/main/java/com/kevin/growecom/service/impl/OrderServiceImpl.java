package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.order.CreateOrderRequest;
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.model.*;
import com.kevin.growecom.repository.InventoryRepository;
import com.kevin.growecom.repository.OrderRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.repository.UserRepository;
import com.kevin.growecom.service.blueprint.OrderService;
import com.kevin.growecom.util.enum2.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find user with id"  + id));
        return OrderResponse.builder().id(order.getId()).status(order.getStatus()).totalPrice(order.getTotalPrice()).build();
    }

    @Override
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .user(user)
                .build();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Long productId = itemRequest.getProductId();
            Integer itemQuantity = itemRequest.getQuantity();

            List<Inventory> inventories = inventoryRepository.findByProductId(productId);
            Inventory inventory = inventories.stream()
                    .filter(inv -> inv.getQuantity() >= itemQuantity)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Not enough stock"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Integer inventoryQuantity = inventory.getQuantity();
            BigDecimal productPrice = product.getPrice();

            if ( inventoryQuantity < itemQuantity) {
                throw new RuntimeException("Not enough stock for : " + product.getName());
            }
            inventory.setQuantity(inventory.getQuantity() - itemRequest.getQuantity());
            inventoryRepository.save(inventory);
            OrderItem orderItem =  OrderItem.builder()
                    .product(product)
                    .quantity(itemQuantity)
                    .price(productPrice)
                    .order(order)
                    .build();
            orderItems.add(orderItem);
            total = total.add(productPrice.multiply(BigDecimal.valueOf(itemQuantity)));
        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(total);
        return toResponse(orderRepository.save(order));
    }
    private OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> OrderResponse.OrderItemResponse.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .userId(order.getUser().getId())
                .items(itemResponses)
                .build();
    }
}