package com.example.demo.controller;

import com.example.demo.model.AddressModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    
    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> postAddress(@RequestBody AddressModel addressModel,
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