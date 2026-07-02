package com.kevin.growecom.service.impl;

import com.kevin.growecom.exception.BaseException;
import com.kevin.growecom.model.RefreshToken;
import com.kevin.growecom.model.User;
import com.kevin.growecom.repository.RefreshTokenRepository;
import com.kevin.growecom.service.RefreshTokenService;
import com.kevin.growecom.util.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.jwt.refresh-expiration-days}")
    private Long REFRESH_EXPIRATION_DAYS;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(REFRESH_EXPIRATION_DAYS, ChronoUnit.DAYS))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BaseException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BaseException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        return refreshToken;
    }

    @Override
    @Transactional
    public void revokeToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
