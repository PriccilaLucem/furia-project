package com.example.demo.controller;

import com.example.demo.config.security.JwtService;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.AdminModel;
import com.example.demo.service.AdminService;
import com.example.demo.util.Authorization;
import org.apache.coyote.BadRequestException;
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

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdminModel admin){
        try{
            UUID id = adminService.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id, "message", "Admin created!"));
        }catch (BadRequestException e){
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login){
        try{
            AdminModel admin = adminService.findByEmail(login.getEmail());
            if(!Authorization.checkPassword(login.getPassword(), admin.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials!"));
            }
            String token = jwtService.generateAdminToken(admin);
            return ResponseEntity.ok(Map.of("token", token));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials!"));
        }
    }
}
