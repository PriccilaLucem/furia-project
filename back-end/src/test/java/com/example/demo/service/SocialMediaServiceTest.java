package com.example.demo.service;

import com.example.demo.model.SocialMediaModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.SocialMediaRepository;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SocialMediaServiceTest {

    @Mock
    private SocialMediaRepository socialMediaRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private SocialMediaService socialMediaService;

    @Test
    void shouldCreateSocialMediaSuccessfully() {
        UUID userId = UUID.randomUUID();
        Long socialId = 1L;

        SocialMediaModel socialMediaModel = new SocialMediaModel();
        socialMediaModel.setInstagramHandle("@teste");

        SocialMediaModel savedSocial = new SocialMediaModel();
        savedSocial.setId(socialId);

        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setId(userId);

        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(userInfo));
        when(socialMediaRepository.save(socialMediaModel)).thenReturn(savedSocial);

        Long resultId = socialMediaService.create(socialMediaModel, userId);

        assertEquals(socialId, resultId);
        verify(userInfoRepository).findById(userId);
        verify(socialMediaRepository).save(socialMediaModel);
        verify(userInfoRepository).save(userInfo);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        SocialMediaModel socialMediaModel = new SocialMediaModel();

        when(userInfoRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            socialMediaService.create(socialMediaModel, userId);
        });

        assertEquals("User Not Found", exception.getMessage());
        verify(userInfoRepository).findById(userId);
        verify(socialMediaRepository, never()).save(any());
        verify(userInfoRepository, never()).save(any());
    }
}
