package com.example.demo.controller;

import com.example.demo.model.SocialMediaModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.SocialMediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Social Media Management", description = "Endpoints for managing user social media accounts")
@SecurityRequirement(name = "bearerAuth")
public class SocialMediaController {
    private static final Logger logger = LoggerFactory.getLogger(SocialMediaController.class);

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    @Operation(
        summary = "Create social media account",
        description = "Links a social media account to the authenticated user",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Social media account details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SocialMediaModel.class)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Social media account linked successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"id\": 123, \"message\": \"Successfully created social media\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"error\": \"Missing required fields\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"error\": \"Authentication failed\"}")
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict - Social media account already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"error\": \"Account already linked\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"error\": \"Internal server error\"}")
            )
        )
    })
    public ResponseEntity<?> create(
        @RequestBody SocialMediaModel socialMediaModel,
        @Parameter(
            description = "JWT authorization token",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
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