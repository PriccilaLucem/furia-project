package com.example.demo.controller;

import com.example.demo.config.security.JwtService;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.AdminModel;
import com.example.demo.service.AdminService;
import com.example.demo.util.Authorization;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void shouldCreateAdminSuccessfully() throws Exception {
        UUID adminId = UUID.randomUUID();
        AdminModel admin = new AdminModel();
        admin.setEmail("admin@example.com");
        admin.setPassword("password123");

        when(adminService.save(any(AdminModel.class))).thenReturn(adminId);

        ResponseEntity<?> response = adminController.create(admin);

        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(adminId, body.get("id"));
        assertEquals("Admin created!", body.get("message"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        AdminModel admin = new AdminModel();
        admin.setEmail("invalid-email");
        admin.setPassword("123");

        when(adminService.save(any(AdminModel.class)))
                .thenThrow(new BadRequestException("Invalid data"));

        ResponseEntity<?> response = adminController.create(admin);

        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Invalid data", body.get("message"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AdminModel admin = new AdminModel();
        admin.setEmail("admin@example.com");
        admin.setPassword("hashedPassword");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("admin@example.com");
        loginDTO.setPassword("plainPassword");

        when(adminService.findByEmail(loginDTO.getEmail())).thenReturn(admin);
        try (MockedStatic<Authorization> mockedAuth = Mockito.mockStatic(Authorization.class)) {
            mockedAuth.when(() -> Authorization.checkPassword(loginDTO.getPassword(), admin.getPassword()))
                    .thenReturn(true);
            when(jwtService.generateAdminToken(admin)).thenReturn("mocked-jwt-token");

            ResponseEntity<?> response = adminController.login(loginDTO);

            Map<String, Object> body = (Map<String, Object>) response.getBody();

            assertEquals(200, response.getStatusCode().value());
            assertNotNull(body);
            assertEquals("mocked-jwt-token", body.get("token"));
        }
    }

    @Test
    void shouldReturnBadRequestOnInvalidLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("admin@example.com");
        loginDTO.setPassword("wrongPassword");

        when(adminService.findByEmail(loginDTO.getEmail())).thenThrow(new RuntimeException("Email not found"));

        ResponseEntity<?> response = adminController.login(loginDTO);

        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Invalid credentials!", body.get("message"));
    }
}
