package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;
import com.kevin.growecom.dto.cart.AddToCartRequest;
import com.kevin.growecom.dto.cart.CartResponse;
import com.kevin.growecom.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ApiResponse.ok("Get cart successfully", cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return ApiResponse.ok("Added to cart successfully", cartService.addToCart(request));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @PathVariable Long productId,
            @RequestBody Integer quantity) {
        return ApiResponse.ok("Updated quantity successfully", cartService.updateQuantity(productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(@PathVariable Long productId) {
        return ApiResponse.ok("Removed item from cart successfully", cartService.removeCartItem(productId));
    }
}