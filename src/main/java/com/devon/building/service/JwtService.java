package com.devon.building.service;


import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateAccessToken(Authentication authentication);
    String extractUsername(String token);
}
