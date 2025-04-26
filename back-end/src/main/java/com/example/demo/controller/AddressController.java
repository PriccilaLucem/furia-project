package com.example.demo.controller;

import com.example.demo.model.AddressModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtService jwtService;


    @PostMapping
    public ResponseEntity<?> postAddress(@RequestBody AddressModel addressModel,
                                         @RequestHeader("Authorization") String token) {
        try {
            UUID userId = jwtService.extractUserId(token);
            Long id = addressService.save(addressModel, userId);
            return ResponseEntity.ok().body(Map.of("id", id, "message", "success"));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal Server Error"));
        }
    }

}
