package com.kevin.growecom.service;

import com.kevin.growecom.dto.auth.AuthResponse;
import com.kevin.growecom.dto.auth.LoginRequest;
import com.kevin.growecom.dto.auth.RefreshRequest;
import com.kevin.growecom.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse createUser(RegisterRequest request);

    AuthResponse loginUser(LoginRequest request);

    AuthResponse refreshToken(RefreshRequest request);

    void logOutUser();
}
