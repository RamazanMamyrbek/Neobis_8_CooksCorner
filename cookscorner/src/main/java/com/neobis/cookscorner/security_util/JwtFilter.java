package com.neobis.cookscorner.security_util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neobis.cookscorner.dtos.ApiErrorResponse;
import com.neobis.cookscorner.exceptions.JwtVerificationException;
import com.neobis.cookscorner.services.ApiUserDetailsService;
import com.neobis.cookscorner.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ApiUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        try {
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwtToken = authHeader.substring(7);
                String username = jwtService.extractUsername(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                    );
                }
            }
        }catch (UsernameNotFoundException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(ApiErrorResponse
                    .builder()
                    .message("User not found")
                    .timestamp(System.currentTimeMillis())
                    .build()));
            return;
        }catch (ExpiredJwtException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(ApiErrorResponse
                    .builder()
                    .message("Jwt expired")
                    .timestamp(System.currentTimeMillis())
                    .build()));
            return;
        } catch (MalformedJwtException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(ApiErrorResponse
                    .builder()
                    .message("Jwt invalid format")
                    .timestamp(System.currentTimeMillis())
                    .build()));
            return;
        }


        filterChain.doFilter(request, response);
    }
}
