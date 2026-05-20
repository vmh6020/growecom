package com.kevin.growecom.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Tắt cơ chế chống giả mạo (để test Postman cho dễ)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // MỞ TOANG: Cho phép mọi request đi qua mà không cần Token
                );
        return httpSecurity.build();
    }
}
