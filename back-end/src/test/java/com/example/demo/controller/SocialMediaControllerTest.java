package com.example.demo.controller;

import com.example.demo.model.SocialMediaModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.SocialMediaService;
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
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class SocialMediaControllerTest {

    @Mock
    private SocialMediaService socialMediaService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private SocialMediaController socialMediaController;

    @Test
    void shouldCreateSocialMediaSuccessfully() {
        UUID userId = UUID.randomUUID();
        Long socialMediaId = 456L;
        String token = "Bearer fake-jwt-token";

        SocialMediaModel socialMedia = new SocialMediaModel();
        socialMedia.setInstagramHandle("@teste");

        Mockito.when(jwtService.extractUserId(anyString())).thenReturn(userId);
        Mockito.when(socialMediaService.create(any(SocialMediaModel.class), any(UUID.class))).thenReturn(socialMediaId);

        ResponseEntity<?> response = socialMediaController.create(socialMedia, token);

        assertEquals(201, response.getStatusCode().value());

        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertNotNull(body);
        assertEquals(socialMediaId, body.get("id"));
        assertEquals("Successfully created social media", body.get("message"));
    }
}
