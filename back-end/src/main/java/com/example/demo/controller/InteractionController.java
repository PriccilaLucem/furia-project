package com.example.demo.controller;

import com.example.demo.model.InteractionModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interaction")
public class InteractionController {
    private static final Logger logger = LoggerFactory.getLogger(InteractionController.class);

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> createInteraction(@RequestBody InteractionModel interactionModel,
                                             @RequestHeader("Authorization") String token) {
        String sanitizedToken = token != null && token.length() > 10 ? 
            token.substring(0, 5) + "..." + token.substring(token.length() - 5) : "[invalid-token]";
        
        logger.info("Starting interaction creation. Token: {}", sanitizedToken);
        try {
            UUID userId = jwtService.extractUserId(token);
            logger.debug("Extracted user ID: {}", userId);
            
            if (userId == null) {
                logger.warn("Invalid user ID extracted from token: {}", sanitizedToken);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body(Map.of("message", "Invalid authentication"));
            }

            long id = interactionService.createInteractionService(interactionModel, userId);
            logger.info("Successfully created interaction. ID: {}, User: {}", 
                       id, userId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(Map.of("id", id, "message", "Interaction created"));

        } catch (SecurityException e) {
            logger.warn("Security exception during interaction creation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body(Map.of("message", "Authentication failed"));
                   
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(Map.of("message", e.getMessage()));
                   
        } catch (Exception e) {
            logger.error("Unexpected error during interaction creation. User: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("message", "Internal Server Error"));
        } finally {
            logger.debug("Completed interaction creation attempt");
        }
    }
}