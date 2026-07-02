package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.order.CreateOrderRequest;
import com.kevin.growecom.dto.order.UpdateOrderRequest;
import com.kevin.growecom.dto.order.OrderResponse;
import com.kevin.growecom.exception.BaseException;
import com.kevin.growecom.model.*;
import com.kevin.growecom.repository.InventoryRepository;
import com.kevin.growecom.repository.OrderRepository;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.repository.UserRepository;
import com.kevin.growecom.service.OrderService;
import com.kevin.growecom.service.UserService;
import com.kevin.growecom.util.enums.ErrorCode;
import com.kevin.growecom.util.enums.OrderStatus;
import com.kevin.growecom.util.enums.CartStatus;
import com.kevin.growecom.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserService userService;
    private final com.kevin.growecom.service.CartService cartService;

    @Override
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
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
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
        order.getOrderItems().clear();
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrderResponse create() {
        User user = userService.getCurrentUser();
        com.kevin.growecom.dto.cart.CartResponse cart = cartService.getCart();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BaseException(ErrorCode.CART_IS_EMPTY);
        }

        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .user(user)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (com.kevin.growecom.dto.cart.CartResponse.CartItemDto cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
            Integer itemQuantity = cartItem.getQuantity();

            List<Inventory> inventories = inventoryRepository.findByProductId(product.getId());
            Inventory inventory = inventories.stream()
                    .filter(inv -> inv.getQuantity() >= itemQuantity)
                    .findFirst()
                    .orElseThrow(() -> new BaseException(ErrorCode.INSUFFICIENT_STOCK));

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
        orderRepository.save(order);

        cartService.clearCart();

        return toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse update(UpdateOrderRequest request) {
        Long orderId = request.getId();
        User user = userService.getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
        
        List<UpdateOrderRequest.UpdateOrderItemRequest> requestOrderItems = request.getOrderItems();
        List<Long> requestProductIds = requestOrderItems.stream()
                .map(UpdateOrderRequest.UpdateOrderItemRequest::getProductId)
                .toList();

        List<OrderItem> dbOrderItems = order.getOrderItems();
        dbOrderItems.removeIf(dbItem -> !requestProductIds.contains(dbItem.getProduct().getId()));

        Map<Long, OrderItem> dbItemMap = dbOrderItems.stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), item -> item));

        BigDecimal total = BigDecimal.ZERO;

        for (var itemRequest : requestOrderItems) {
            OrderItem existingItem = dbItemMap.get(itemRequest.getProductId());
            if (existingItem != null) {
                existingItem.setQuantity(itemRequest.getQuantity());
                existingItem.setPrice(existingItem.getProduct().getPrice());
                total = total.add(existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            } else {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));
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
