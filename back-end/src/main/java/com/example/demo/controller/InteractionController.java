package com.example.demo.controller;

import com.example.demo.model.InteractionModel;
import com.example.demo.security.JwtService;
import com.example.demo.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interaction")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/creat")
    public ResponseEntity<?> createInteraction(@RequestBody InteractionModel interactionModel,
                                               @RequestHeader("Authorization") String token) {
        try{
            UUID userId = jwtService.extractUserId(token);
            long id = interactionService.createInteractionService(interactionModel, userId);
            return  ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id, "message", "Interaction created"));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal Server Error"));
        }
    }
}
