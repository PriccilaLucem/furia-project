package com.example.demo.controller;


import com.example.demo.model.UserInfoModel;
import com.example.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserInfoModel userInfoModel) {
        try {
            UUID id = userInfoService.createUser(userInfoModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id, "message", "User created successfully"));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }
}
