package com.neobis.cookscorner.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;

public interface JwtService {
    String generateToken(UserDetails userDetails);

    Claims extractClaims(String jwtToken);

    String extractUsername(String jwtToken);

    Key getSigningKey();


}
