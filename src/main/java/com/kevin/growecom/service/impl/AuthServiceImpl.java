package com.kevin.growecom.service.impl;

import com.kevin.growecom.security.UserPrinciple;
import com.kevin.growecom.dto.auth.AuthResponse;
import com.kevin.growecom.dto.auth.LoginRequest;
import com.kevin.growecom.dto.auth.RefreshRequest;
import com.kevin.growecom.dto.auth.RegisterRequest;
import com.kevin.growecom.repository.RefreshTokenRepository;
import com.kevin.growecom.service.UserService;
import com.kevin.growecom.model.RefreshToken;
import com.kevin.growecom.model.User;
import com.kevin.growecom.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse createUser(RegisterRequest request) {
        User newUser = userService.registerNewUser(request);
        String accessToken = jwtService.generateToken(new UserPrinciple(newUser));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(newUser);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthResponse loginUser(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
        String accessToken = jwtService.generateToken(principle);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principle.getUser());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshRequest request) {
        RefreshToken oldToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = oldToken.getUser();

        refreshTokenService.revokeToken(oldToken);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        String newAccessToken = jwtService.generateToken(new UserPrinciple(user));

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    @Override
    @Transactional
    public void logOutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getSubject();
        User user = userService.findByEmail(email);
        refreshTokenRepository.deleteByUser(user);
        //access token delete frontend
    }
}
