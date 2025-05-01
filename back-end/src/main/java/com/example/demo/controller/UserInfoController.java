package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.UserInfoModel;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.Authorization;
import com.example.demo.config.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "Endpoints for user registration and authentication")
public class UserInfoController {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    @Operation(
        summary = "Register new user",
        description = "Creates a new user account",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserInfoModel.class),
                examples = @ExampleObject(
                    value = "{\"email\": \"user@example.com\", \"password\": \"securePassword123\", \"name\": \"John Doe\"}"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"id\": \"a1b2c3d4\", \"message\": \"User created successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"error\": \"Email already registered\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"error\": \"Internal server error\"}"
                )
            )
        )
    })
    public ResponseEntity<?> create(@RequestBody UserInfoModel userInfoModel) {
        logger.info("User creation request received for email: {}", userInfoModel.getEmail());
        
        try {
            UUID id = userInfoService.createUser(userInfoModel);
            logger.info("User created successfully. ID: {}, Email: {}", id, userInfoModel.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                   .body(Map.of(
                       "id", id,
                       "message", "User created successfully"
                   ));
                   
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid user creation request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(Map.of("error", e.getMessage()));
                   
        } catch (RuntimeException e) {
            logger.error("Business error during user creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(Map.of("error", e.getMessage()));
                   
        } catch (Exception e) {
            logger.error("Unexpected error during user creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Logs in a user and returns JWT token",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginDTO.class),
                examples = @ExampleObject(
                    value = "{\"email\": \"user@example.com\", \"password\": \"securePassword123\"}"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiIs...\", \"userId\": \"a1b2c3d4\", \"message\": \"Login successful\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"error\": \"Invalid credentials\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"error\": \"Internal server error\"}"
                )
            )
        )
    })
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        logger.info("Login attempt for email: {}", login.getEmail());
        
        try {
            UserInfoModel user = userInfoService.getByEmail(login.getEmail());
            logger.debug("User found for email: {}", login.getEmail());
            
            if (!Authorization.checkPassword(login.getPassword(), user.getPassword())) {
                logger.warn("Invalid password attempt for email: {}", login.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body(Map.of("error", "Invalid credentials"));
            }
            
            String token = jwtService.generateToken(user);
            logger.info("Successful login for user ID: {}, Email: {}", user.getId(), user.getEmail());
            
            return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "message", "Login successful"
            ));
            
        } catch (RuntimeException e) {
            logger.warn("Authentication failure for email {}: {}", login.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body(Map.of("error", "Invalid credentials"));
                   
        } catch (Exception e) {
            logger.error("Unexpected error during login for email {}: {}", login.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("error", "Internal server error"));
        }
    }
}