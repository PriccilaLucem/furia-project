package com.example.demo.controller;

import com.example.demo.config.security.JwtService;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.AdminModel;
import com.example.demo.service.AdminService;
import com.example.demo.util.Authorization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Management", description = "Endpoints for administrator operations")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    @Operation(
        summary = "Create admin account",
        description = "Creates a new administrator account with elevated privileges",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Admin details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdminModel.class)
        )
    ))
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Admin created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"id\": \"a1b2c3d4\", \"message\": \"Admin created!\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Email already registered\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Internal server error\"}")
            )
        )
    })
    public ResponseEntity<?> create(@RequestBody AdminModel admin) {
        logger.info("Admin creation request received for email: {}", admin.getEmail());
        
        try {
            UUID id = adminService.save(admin);
            logger.info("Admin created successfully with ID: {}, Email: {}", id, admin.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(Map.of("id", id, "message", "Admin created!"));
                   
        } catch (BadRequestException e) {
            logger.warn("Bad request during admin creation for email {}: {}", admin.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                   .body(Map.of("message", e.getMessage()));
                   
        } catch (Exception e) {
            logger.error("Unexpected error during admin creation for email {}: {}", admin.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                   .body(Map.of("message", "Internal server error"));
        }
    }

    @Operation(
        summary = "Admin login",
        description = "Authenticates administrator and returns JWT token",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginDTO.class)
        )
    ))
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Invalid credentials!\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Invalid credentials!\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class,
                    example = "{\"message\": \"Internal server error\"}")
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        logger.info("Login attempt for email: {}", login.getEmail());
        
        try {
            AdminModel admin = adminService.findByEmail(login.getEmail());
            logger.debug("Admin found for email: {}", login.getEmail());
            
            if (!Authorization.checkPassword(login.getPassword(), admin.getPassword())) {
                logger.warn("Invalid password attempt for email: {}", login.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body(Map.of("message", "Invalid credentials!"));
            }
            
            String token = jwtService.generateAdminToken(admin);
            logger.info("Successful login for admin ID: {}, Email: {}", admin.getId(), admin.getEmail());
            
            return ResponseEntity.ok(Map.of("token", token));
            
        } catch (RuntimeException e) {
            logger.warn("Failed login attempt for email {}: {}", login.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                   .body(Map.of("message", "Invalid credentials!"));
                   
        } catch (Exception e) {
            logger.error("Unexpected error during login for email {}: {}", login.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                   .body(Map.of("message", "Internal server error"));
        }
    }
}