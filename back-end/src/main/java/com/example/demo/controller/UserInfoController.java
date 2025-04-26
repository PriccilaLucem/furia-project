package com.example.demo.controller;


import com.example.demo.dto.LoginDTO;
import com.example.demo.model.UserInfoModel;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.config.security.JwtService;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserInfoModel userInfoModel) {
        try {
            UUID id = userInfoService.createUser(userInfoModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id, "message", "User created successfully"));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        try{
            UserInfoModel user = userInfoService.getByEmail(login.getEmail());
            if(!Authorization.checkPassword(login.getPassword(), user.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
            }
            String token = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", token));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
