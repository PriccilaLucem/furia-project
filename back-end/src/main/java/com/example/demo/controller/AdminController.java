package com.example.demo.controller;

import com.example.demo.config.security.JwtService;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.AdminModel;
import com.example.demo.service.AdminService;
import com.example.demo.util.Authorization;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
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