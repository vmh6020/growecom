package com.kevin.growecom.security;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(s -> s
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/categories/**").permitAll()
            .requestMatchers("/api/v1/products/**").permitAll()
            .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/refresh").permitAll()
            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        // .requestMatchers("/api/v1/auth/logout").authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        // .jwt(Customizer.withDefaults())
        );
    return http.build();
  }

  @Bean
  public JwtEncoder jwtEncoder(@Value("${app.jwt.secret}") String secret) {
    SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    OctetSequenceKey jwk = new OctetSequenceKey.Builder(key)
        .algorithm(JWSAlgorithm.HS256)
        .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks); // encoder use Nimbus library
  }

   @Bean
   public JwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secret) {
   SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
   return NimbusJwtDecoder.withSecretKey(key).build();
   }
   @Bean
   public PasswordEncoder passwordEncoder() {
   return new BCryptPasswordEncoder();
   }
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

   @Bean
   public JwtAuthenticationConverter jwtAuthenticationConverter() {
     JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
     converter.setAuthoritiesClaimName("role"); // scope -> role
     converter.setAuthorityPrefix("ROLE_"); // SCOPE_ -> ROLE_
     JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
     jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
   return jwtConverter;
   }
}
