package com.example.demo.service;

import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserAndReturnId() {
        // Arrange
        UserInfoModel user = new UserInfoModel();
        user.setId(UUID.randomUUID());
        user.setName("John");
        user.setPassword("123456");
        user.setEmail("john@example.com");
        user.setPhone("+5511999999999");

        when(userInfoRepository.save(any(UserInfoModel.class))).thenReturn(user);

        // Act
        UUID id = userInfoService.createUser(user);

        // Assert
        assertNotNull(id);
        verify(userInfoRepository, times(1)).save(any(UserInfoModel.class));
    }

    @Test
    void shouldThrowIfEmailInvalid() {
        UserInfoModel user = new UserInfoModel();
        user.setName("John");
        user.setPassword("123456");
        user.setEmail("invalid-email");
        user.setPhone("11999999999");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userInfoService.createUser(user);
        });

        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    void shouldGetUserById() {
        UUID id = UUID.randomUUID();
        UserInfoModel user = new UserInfoModel();
        user.setId(id);

        when(userInfoRepository.findById(id)).thenReturn(Optional.of(user));

        UserInfoModel result = userInfoService.getUserInfo(id);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userInfoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userInfoService.getUserInfo(id);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
