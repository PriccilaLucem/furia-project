package com.example.demo.controller;

import com.example.demo.model.SocialMediaModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.SocialMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/social-media")
public class SocialMediaController {
    private static final Logger logger = LoggerFactory.getLogger(SocialMediaController.class);

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SocialMediaModel socialMediaModel,
                                   @RequestHeader("Authorization") String token) {
        String sanitizedToken = token != null && token.length() > 10 ? 
            token.substring(0, 5) + "..." + token.substring(token.length() - 5) : "[invalid-token]";

        logger.info("Starting social media creation request. Token: {}", sanitizedToken);

        try {
            UUID userId = jwtService.extractUserId(token);
            logger.debug("Extracted user ID: {} from token", userId);
            
            if (userId == null) {
                logger.warn("Invalid user ID extracted from token: {}", sanitizedToken);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body(Map.of("error", "Invalid authentication token"));
            }

            Long id = socialMediaService.create(socialMediaModel, userId);
            logger.info("Successfully created social media entry. ID: {}, User: {}",
                       id, userId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(Map.of(
                       "id", id,
                       "message", "Successfully created social media"
                   ));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(Map.of("error", e.getMessage()));
                   
        } catch (SecurityException e) {
            logger.warn("Authentication failure: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body(Map.of("error", "Authentication failed"));
                   
        } catch (RuntimeException e) {
            logger.error("Business logic error during social media creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(Map.of("error", e.getMessage()));
                   
        } catch (Exception e) {
            logger.error("Unexpected error during social media creation. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("error", "Internal server error"));
        } finally {
            logger.debug("Completed social media creation attempt for token: {}", sanitizedToken);
        }
    }
}