package com.example.demo.controller;

import com.example.demo.model.InteractionModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.InteractionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InteractionControllerTest {

    @Mock
    private InteractionService interactionService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private InteractionController interactionController;

    @Test
    void shouldCreateInteractionSuccessfully() {
        UUID userId = UUID.randomUUID();
        long interactionId = 101L;
        String token = "Bearer fake-jwt-token";

        InteractionModel interactionModel = new InteractionModel();
        interactionModel.setAlreadyWentToFuriaEvent(true);
        interactionModel.setBoughtItems(true);
        interactionModel.setEFuriaClubMember(false);

        when(jwtService.extractUserId(Mockito.anyString())).thenReturn(userId);
        when(interactionService.createInteractionService(any(InteractionModel.class), any(UUID.class)))
                .thenReturn(interactionId);

        ResponseEntity<?> response = interactionController.createInteraction(interactionModel, token);

        assertEquals(201, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(interactionId, responseBody.get("id"));
        assertEquals("Interaction created", responseBody.get("message"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenExceptionOccurs() {
        String token = "Bearer fake-jwt-token";
        InteractionModel interactionModel = new InteractionModel();

        when(jwtService.extractUserId(Mockito.anyString())).thenThrow(new RuntimeException("Token error"));

        ResponseEntity<?> response = interactionController.createInteraction(interactionModel, token);

        assertEquals(500, response.getStatusCode().value());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Internal Server Error", responseBody.get("message"));
    }
}
