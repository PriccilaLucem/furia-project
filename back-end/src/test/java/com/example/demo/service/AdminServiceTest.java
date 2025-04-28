package com.example.demo.service;

import com.example.demo.model.AdminModel;
import com.example.demo.repository.AdminRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldHashPasswordAndReturnId_WhenAdminIsValid() throws BadRequestException {
        // Arrange
        AdminModel admin = new AdminModel();
        admin.setEmail("test@example.com");
        admin.setPassword("securePassword");

        AdminModel savedAdmin = new AdminModel();
        savedAdmin.setId(UUID.randomUUID());

        when(adminRepository.save(any(AdminModel.class))).thenReturn(savedAdmin);

        // Act
        UUID result = adminService.save(admin);

        // Assert
        assertNotNull(result);
        ArgumentCaptor<AdminModel> adminCaptor = ArgumentCaptor.forClass(AdminModel.class);
        verify(adminRepository).save(adminCaptor.capture());
        assertNotEquals("securePassword", adminCaptor.getValue().getPassword()); // Should be hashed
    }

    @Test
    void save_ShouldThrowBadRequestException_WhenPasswordIsTooShort() {
        // Arrange
        AdminModel admin = new AdminModel();
        admin.setEmail("test@example.com");
        admin.setPassword("123");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            adminService.save(admin);
        });

        assertEquals("Password must be at least 6 characters", exception.getMessage());
        verify(adminRepository, never()).save(any());
    }

    @Test
    void save_ShouldThrowBadRequestException_WhenEmailIsInvalid() {
        // Arrange
        AdminModel admin = new AdminModel();
        admin.setEmail("invalid-email");
        admin.setPassword("securePassword");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            adminService.save(admin);
        });

        assertEquals("Invalid email", exception.getMessage());
        verify(adminRepository, never()).save(any());
    }

    @Test
    void findByEmail_ShouldReturnAdmin_WhenEmailExists() {
        // Arrange
        AdminModel admin = new AdminModel();
        admin.setId(UUID.randomUUID());
        admin.setEmail("found@example.com");

        when(adminRepository.findByEmail("found@example.com")).thenReturn(Optional.of(admin));

        // Act
        AdminModel result = adminService.findByEmail("found@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("found@example.com", result.getEmail());
    }

    @Test
    void findByEmail_ShouldThrowRuntimeException_WhenEmailDoesNotExist() {
        // Arrange
        when(adminRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.findByEmail("notfound@example.com");
        });

        assertEquals("Email not found", exception.getMessage());
    }
}
