package com.kevin.growecom.service.impl;

import com.kevin.growecom.auth.UserPrinciple;
import com.kevin.growecom.dto.auth.AuthResponse;
import com.kevin.growecom.dto.auth.LoginRequest;
import com.kevin.growecom.dto.auth.RegisterRequest;
import com.kevin.growecom.model.User;
import com.kevin.growecom.repository.UserRepository;
import com.kevin.growecom.service.JwtService;
import com.kevin.growecom.service.blueprint.AuthService;
import com.kevin.growecom.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthResponse createUser(RegisterRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();
        // email existed ?
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email already existed: " + email);
        }
        // Hash password = PasswordEncoder
        String encodedPassword = passwordEncoder.encode(password);
        // Save User DB role = User
        User newUser = userRepository.save(User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build());
        // create token from JwtService
        String accessToken = jwtService.generateToken(new UserPrinciple(newUser));

        // return AuthResponse
        return AuthResponse.builder().accessToken(accessToken).build();
    }

    @Override
    public AuthResponse loginUser(LoginRequest request) {
        // use AuthenticationManager
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // wrong credentials -> auto throw exception
        UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
        String token = jwtService.generateToken(principle);
        // AM auto call UserDetailsService -> load user -> compare password brcyted
        // -> throw BadCredentialsException if wrong
        return AuthResponse.builder().accessToken(token).build();
    }
}
