package com.kevin.growecom.service;

import com.kevin.growecom.dto.CreateOrderRequest;
import com.kevin.growecom.entity.*;
import com.kevin.growecom.repository.OrderRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .user(user)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Integer itemQuantity = itemRequest.getQuantity();
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            BigDecimal productPrice= product.getPrice();
            OrderItem orderItem =  OrderItem.builder()
                    .product(product)
                    .quantity(itemQuantity)
                    .price(productPrice)
                    .order(order)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
            orderItems.add(orderItem);
            total = total.add(productPrice.multiply(BigDecimal.valueOf(itemQuantity)));
        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(total);
        return order;
    }
}

//        Nhận vào userId + list {productId, quantity}
//        Tìm User
//        Với mỗi product → check inventory còn đủ không
//        Tạo Order + OrderItems
//        Trừ inventory
//        Lưu DB    }


