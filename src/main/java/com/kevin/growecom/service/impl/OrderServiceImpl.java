package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.order.CreateOrderRequest;
import com.kevin.growecom.dto.order.UpdateOrderRequest;
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.model.*;
import com.kevin.growecom.repository.InventoryRepository;
import com.kevin.growecom.repository.OrderRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.repository.UserRepository;
import com.kevin.growecom.service.blueprint.OrderService;
import com.kevin.growecom.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find order with id " + id));
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order - " + id + " not found"));

        // Nếu dùng (orphanRemoval = true), lệnh clear này sẽ xóa sạch item dưới DB
        order.getOrderItems().clear();
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
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

            inventory.setQuantity(inventory.getQuantity() - itemQuantity);
            inventoryRepository.save(inventory);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemQuantity)
                    .price(product.getPrice())
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemQuantity)));
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse update(UpdateOrderRequest request) {
        Long orderId = request.getId();
        Long userId = request.getUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order - " + orderId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User - " + userId + " not found"));

        List<UpdateOrderRequest.UpdateOrderItemRequest> requestOrderItems = request.getOrderItems();

        List<Long> requestProductIds = requestOrderItems.stream()
                .map(UpdateOrderRequest.UpdateOrderItemRequest::getProductId)
                .toList();

        List<OrderItem> dbOrderItems = order.getOrderItems();

        dbOrderItems.removeIf(dbItem -> !requestProductIds.contains(dbItem.getProduct().getId()));

        Map<Long, OrderItem> dbItemMap = dbOrderItems.stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        item -> item
                ));

        BigDecimal total = BigDecimal.ZERO;

        for (var itemRequest : requestOrderItems) {
            OrderItem existingItem = dbItemMap.get(itemRequest.getProductId());

            if (existingItem != null) {
                existingItem.setQuantity(itemRequest.getQuantity());
                existingItem.setPrice(existingItem.getProduct().getPrice());
                total = total.add(existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            } else {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product - " + itemRequest.getProductId() + " does not exist"));

                OrderItem newItem = OrderItem.builder()
                        .product(product)
                        .quantity(itemRequest.getQuantity())
                        .price(product.getPrice())
                        .order(order)
                        .build();

                dbOrderItems.add(newItem);
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            }
        }

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
