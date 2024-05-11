package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.exceptions.JwtVerificationException;
import com.neobis.cookscorner.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject("jwt")
                .setClaims(Map.of("email", userDetails.getUsername()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofMinutes(30).toMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    @Override
    public String extractUsername(String jwtToken) {
        return extractClaims(jwtToken).get("email").toString();
    }


    @Override
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
