package com.kevin.growecom.service.impl;

import com.kevin.growecom.security.UserPrinciple;
import com.kevin.growecom.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${app.jwt.expiration-minutes}")
    private Long EXPIRATION_MINUTE;

    private final JwtEncoder jwtEncoder;

    @Override
    public String generateToken(UserPrinciple principle)  {
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(principle.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(EXPIRATION_MINUTE)))
                .claim("role", principle.getUser().getRole().name())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
