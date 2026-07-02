package com.kevin.growecom.service;

import com.kevin.growecom.model.RefreshToken;
import com.kevin.growecom.model.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void revokeToken(RefreshToken refreshToken);
}
