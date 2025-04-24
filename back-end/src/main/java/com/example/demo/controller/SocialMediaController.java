package com.example.demo.controller;


import com.example.demo.model.SocialMediaModel;
import com.example.demo.security.JwtService;
import com.example.demo.service.SocialMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/social-media")
public class SocialMediaController {

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SocialMediaModel socialMediaModel,
                                    @RequestHeader("Authorization") String token) {
        try{
            UUID userId = jwtService.extractUserId(token);
            Long id = socialMediaService.create(socialMediaModel, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id, "message", "Successfully created social media"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
