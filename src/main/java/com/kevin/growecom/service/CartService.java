package com.kevin.growecom.service;

import com.kevin.growecom.dto.cart.AddToCartRequest;
import com.kevin.growecom.dto.cart.CartResponse;

public interface CartService {
    CartResponse getCart();
    CartResponse addToCart(AddToCartRequest request);
    CartResponse updateQuantity(Long productId, Integer quantity);
    CartResponse removeCartItem(Long productId);
    void clearCart();
}
