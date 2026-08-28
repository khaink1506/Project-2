package com.devon.building.service.impl;

import com.devon.building.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service

public class JwtServiceImpl implements JwtService {

    private final SecretKey signingKey;
    private final long expiration;

    public JwtServiceImpl(@Value("${jwt.secret-key}") String secretKey,
                          @Value("${jwt.expiration}") long expiration
    ) {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(bytes);
        this.expiration = expiration;
    }

    @Override
    public String generateAccessToken(Authentication authentication) {

        return Jwts.builder()
                .subject(authentication.getName())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String extractUsername(String token){
        return parseToken(token).getSubject();
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
