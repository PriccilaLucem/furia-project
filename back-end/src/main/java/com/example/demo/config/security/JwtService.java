package com.example.demo.config.security;

import com.example.demo.model.AdminModel;
import com.example.demo.model.UserInfoModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas
    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret.key}") String base64EncodedSecret) {
        if (base64EncodedSecret == null || base64EncodedSecret.length() < 32) {
            throw new IllegalArgumentException("Secret key must be at least 32 characters (256 bits) long");
        }
        
        try {
            byte[] decodedKey = Decoders.BASE64.decode(base64EncodedSecret);
            this.secretKey = Keys.hmacShaKeyFor(decodedKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64-encoded secret key", e);
        }
    }

    public String generateToken(UserInfoModel user) {
        return buildToken(user.getEmail(), user.getId().toString(), "ROLE_USER");
    }

    public String generateAdminToken(AdminModel admin) {
        return buildToken(admin.getEmail(), admin.getId().toString(), "ROLE_ADMIN");
    }

    private String buildToken(String 
    subject, String id, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public UUID extractUserId(String token) {
        if (isTokenExpired(token)) {
            throw new ExpiredJwtException(null, null, "Token expired");
        }
        return UUID.fromString(extractClaim(cleanToken(token), Claims::getId));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(cleanToken(token))
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired", e);
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException e) {
            throw new JwtException("Invalid token", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Token cannot be empty", e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}