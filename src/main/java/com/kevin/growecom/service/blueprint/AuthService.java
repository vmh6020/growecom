package com.kevin.growecom.service.blueprint;

import com.kevin.growecom.dto.auth.AuthResponse;
import com.kevin.growecom.dto.auth.LoginRequest;
import com.kevin.growecom.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse createUser(RegisterRequest request);

    AuthResponse loginUser(LoginRequest request);
}
