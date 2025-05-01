package com.example.demo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_HEADER = "Authorization";
    private static final String ROLE_CLAIM = "role";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
        logger.info("JwtAuthenticationFilter initialized with JwtService");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        
        final String requestUri = request.getRequestURI();
        logger.debug("Starting authentication for request: {}", requestUri);
        
        try {
            final String authHeader = request.getHeader(AUTH_HEADER);
            
            if (authHeader == null) {
                logger.debug("No Authorization header found for request: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }
            
            if (!authHeader.startsWith(BEARER_PREFIX)) {
                logger.warn("Invalid Authorization header format for request: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }
            
            final String token = authHeader.substring(BEARER_PREFIX.length());
            logger.debug("JWT token extracted for request: {}", requestUri);
            
            if (token.isBlank()) {
                logger.warn("Empty JWT token provided for request: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }
            
            if (!jwtService.isTokenValid(token)) {
                logger.warn("Invalid JWT token provided for request: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }
            
            authenticateUser(request, token);
            logger.info("Successfully authenticated request: {}", requestUri);
            
            filterChain.doFilter(request, response);
            
        } catch (JwtException e) {
            logger.error("JWT processing error for request {}: {}", requestUri, e.getMessage(), e);
            handleJwtException(response, e);
        } catch (Exception e) {
            logger.error("Unexpected authentication error for request {}: {}", requestUri, e.getMessage(), e);
            handleGenericException(response, e);
        } finally {
            logger.debug("Completed authentication processing for request: {}", requestUri);
        }
    }

    private void authenticateUser(HttpServletRequest request, String token) {
        String username = jwtService.extractUsername(token);
        logger.debug("Extracted username from token: {}", username);
        
        if (username == null) {
            logger.warn("No username extracted from token");
            throw new JwtException("Invalid token - no username");
        }
        
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.debug("User {} already authenticated", username);
            return;
        }
        
        String role = jwtService.extractClaim(token, claims -> claims.get(ROLE_CLAIM, String.class));
        logger.debug("Extracted role from token: {}", role);
        
        if (role == null || role.isEmpty()) {
            logger.warn("Missing role in token for user: {}", username);
            throw new JwtException("Missing role claim in token");
        }
        
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(role)
        );
        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            username,
            null,
            authorities
        );
        
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        logger.info("Successfully authenticated user: {} with role: {}", username, role);
    }

    private void handleJwtException(HttpServletResponse response, JwtException e) throws IOException {
        logger.error("JWT authentication failed: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
    }

    private void handleGenericException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Authentication error: {}", e.getMessage(), e);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed: " + e.getMessage());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldNotFilter = path.startsWith("/api/auth") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
        
        if (shouldNotFilter) {
            logger.debug("Skipping JWT filter for path: {}", path);
        }
        
        return shouldNotFilter;
    }
}