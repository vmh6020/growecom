package com.kevin.growecom.service.impl;

import com.kevin.growecom.dto.cart.AddToCartRequest;
import com.kevin.growecom.dto.cart.CartResponse;
import com.kevin.growecom.exception.BaseException;
import com.kevin.growecom.model.Product;
import com.kevin.growecom.model.User;
import com.kevin.growecom.repository.ProductRepository;
import com.kevin.growecom.service.CartService;
import com.kevin.growecom.service.UserService;
import com.kevin.growecom.util.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final UserService userService;

    private String getCartKey(Long userId) {
        return "cart:" + userId;
    }

    @Override
    public CartResponse getCart() {
        User user = userService.getCurrentUser();
        String key = getCartKey(user.getId());

        // Lấy toàn bộ items trong Redis Hash của User
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        BigDecimal total = BigDecimal.ZERO;
        List<CartResponse.CartItemDto> itemDtos = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            Integer quantity = Integer.valueOf(entry.getValue().toString());

            Product product = productRepository.findById(productId).orElse(null);

            if (product == null) {
                // delete product not existed in database
                redisTemplate.opsForHash().delete(key, entry.getKey());
                continue;
            }

            BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(subTotal);

            itemDtos.add(CartResponse.CartItemDto.builder()
                    .id(productId) // Dùng productId làm id cho CartItemDto
                    //TODO: them truong addedAt -> sap sep thu tu
                    .productId(productId)
                    .productName(product.getName())
                    .quantity(quantity)
                    .price(product.getPrice())
                    .subTotal(subTotal)
                    .build());
        }

        return CartResponse.builder()
                .cartId(user.getId()) // Dùng userId làm cartId: 1 user 1 cart
                .items(itemDtos)
                .totalPrice(total)
                .build();
    }

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        User user = userService.getCurrentUser();
        String key = getCartKey(user.getId());
        String field = request.getProductId().toString();

        // Kiểm tra xem sản phẩm có thực sự tồn tại trong DB không trước khi thêm vào giỏ
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BaseException(ErrorCode.RESOURCE_NOT_FOUND));

        Object existingQtyVal = redisTemplate.opsForHash().get(key, field);
        int quantityToAdd = request.getQuantity();

        if (existingQtyVal != null) {
            quantityToAdd += Integer.parseInt(existingQtyVal.toString());
        }

        if (quantityToAdd <= 0) {
            redisTemplate.opsForHash().delete(key, field);
        } else {
            redisTemplate.opsForHash().put(key, field, quantityToAdd);
        }

        return getCart();
    }

    @Override
    public CartResponse updateQuantity(Long productId, Integer quantity) {
        User user = userService.getCurrentUser();
        String key = getCartKey(user.getId());
        String field = productId.toString();

        if (quantity <= 0) {
            redisTemplate.opsForHash().delete(key, field);
        } else {
            // Kiểm tra sản phẩm tồn tại
            if (!productRepository.existsById(productId)) {
                throw new BaseException(ErrorCode.RESOURCE_NOT_FOUND);
            }
            redisTemplate.opsForHash().put(key, field, quantity);
        }

        return getCart();
    }

    @Override
    public CartResponse removeCartItem(Long productId) {
        User user = userService.getCurrentUser();
        String key = getCartKey(user.getId());
        String field = productId.toString();

        redisTemplate.opsForHash().delete(key, field);

        return getCart();
    }

    @Override
    public void clearCart() {
        User user = userService.getCurrentUser();
        String key = getCartKey(user.getId());
        redisTemplate.delete(key);
    }
}
