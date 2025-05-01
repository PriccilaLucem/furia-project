package com.example.demo.controller;

import com.example.demo.model.InteractionModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.InteractionService;
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
@RequestMapping("/api/v1/interaction")
@Tag(name = "Interaction Management", description = "Endpoints for managing user interactions")
@SecurityRequirement(name = "bearerAuth")
public class InteractionController {
    private static final Logger logger = LoggerFactory.getLogger(InteractionController.class);

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    @Operation(
        summary = "Create a new interaction",
        description = "Creates a new user interaction record. Requires JWT authentication.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Interaction details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InteractionModel.class)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Interaction created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"id\": 123, \"message\": \"Interaction created\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Invalid interaction data\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Authentication failed\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Internal Server Error\"}")
            )
        )
    })
    public ResponseEntity<?> createInteraction(
        @RequestBody InteractionModel interactionModel,
        @Parameter(
            description = "JWT authorization token",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
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
            logger.info("Successfully created interaction. ID: {}, User: {}", id, userId);
            
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
            logger.error("Unexpected error during interaction creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("message", "Internal Server Error"));
        } finally {
            logger.debug("Completed interaction creation attempt");
        }
    }
}