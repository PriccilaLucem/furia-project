package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.UserInfoModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.Authorization;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserInfoControllerTest {


    @Mock
    private UserInfoService userInfoService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserInfoController userInfoController;

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        UUID newId = UUID.randomUUID();
        UserInfoModel user = new UserInfoModel();
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPhone("11999999999");

        Mockito.when(userInfoService.createUser(any(UserInfoModel.class))).thenReturn(newId);

        // Act
        ResponseEntity<?> response = userInfoController.create(user);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(newId.toString(), body.get("id").toString());
        assertEquals("User created successfully", body.get("message"));
    }


    @Test
    void shouldLoginAndReturnToken() {
        // Arrange
        String rawPassword = "password";
        String hashedPassword = "password";

        LoginDTO login = new LoginDTO("test@example.com", rawPassword);

        UserInfoModel user = new UserInfoModel();
        user.setId(UUID.randomUUID());
        user.setName("Test");
        user.setEmail(login.getEmail());
        user.setPassword(hashedPassword);
        user.setPhone("11999999999");

        Mockito.when(userInfoService.getByEmail(login.getEmail()))
                .thenReturn(user);
        Mockito.when(jwtService.generateToken(user))
                .thenReturn("fake-jwt-token");

        try (MockedStatic<Authorization> mockedAuth = Mockito.mockStatic(Authorization.class)) {
            mockedAuth.when(() -> Authorization.checkPassword(rawPassword, hashedPassword))
                    .thenReturn(true);

            // Act
            ResponseEntity<?> response = userInfoController.login(login);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertEquals("fake-jwt-token", body.get("token"));
        }
    }
}
