package com.kevin.growecom.controller;

import com.kevin.growecom.dto.ApiResponse;
import com.kevin.growecom.dto.auth.AuthResponse;
import com.kevin.growecom.dto.auth.LoginRequest;
import com.kevin.growecom.dto.auth.RegisterRequest;
import com.kevin.growecom.service.blueprint.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> createUser(@RequestBody RegisterRequest request) {
        return ApiResponse.created("User created successfully", authService.createUser(request));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@RequestBody LoginRequest request) {
        return ApiResponse.success("User authenticated successfully", authService.loginUser(request));
    }
}
