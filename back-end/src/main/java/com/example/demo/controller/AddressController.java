package com.example.demo.controller;

import com.example.demo.model.AddressModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.AddressService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@Tag(name = "Address Management", description = "Endpoints for managing user addresses")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    
    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    @Operation(
        summary = "Create a new address",
        description = "Creates a new address for the authenticated user. Requires JWT authentication.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Address details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddressModel.class)
        )
        ))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Address created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"id\": 1, \"message\": \"success\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"error\": \"Invalid address data\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing JWT token"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"error\": \"Internal Server Error\"}")
            )
        )
    })
    public ResponseEntity<?> postAddress(
        @RequestBody AddressModel addressModel,
        @Parameter(
            description = "JWT authorization token",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader("Authorization") String token) {
        
        logger.info("Iniciando cadastro de endereço para o token: {}", sanitizeToken(token));
        
        try {
            UUID userId = jwtService.extractUserId(token);
            logger.debug("UserID extraído do token: {}", userId);
            
            Long id = addressService.save(addressModel, userId);
            logger.info("Endereço cadastrado com sucesso. ID: {}, UserID: {}", id, userId);
            
            return ResponseEntity.ok().body(Map.of("id", id, "message", "success"));
            
        } catch (RuntimeException e) {
            logger.warn("Erro ao cadastrar endereço: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            
        } catch (Exception e) {
            logger.error("Erro interno ao cadastrar endereço", e);
            return ResponseEntity.internalServerError()
                   .body(Map.of("error", "Internal Server Error"));
        }
    }
    
    private String sanitizeToken(String token) {
        if (token == null || token.length() < 10) {
            return "[invalid-token]";
        }
        return token.substring(0, 5) + "..." + token.substring(token.length() - 5);
    }
}