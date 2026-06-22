package com.kevin.growecom.service;

import com.kevin.growecom.auth.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${app.jwt.expiration-minutes}")
    private Long EXPIRATION_MINUTE;

    private final JwtEncoder jwtEncoder;
    public String generateToken(UserPrinciple principle)  {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(principle.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTE)))
                .claim("role", principle.getUser().getRole().name())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
