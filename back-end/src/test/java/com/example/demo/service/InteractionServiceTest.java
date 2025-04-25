package com.example.demo.service;

import com.example.demo.model.InteractionModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.InteractionRepository;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractionServiceTest {

    @Mock
    private InteractionRepository interactionRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private FanScoreService fanScoreService;

    @InjectMocks
    private InteractionService interactionService;

    @Test
    void shouldCreateInteractionAndUpdateFanScore() {
        UUID userId = UUID.randomUUID();
        Long interactionId = 100L;

        InteractionModel interactionModel = new InteractionModel();
        interactionModel.setAlreadyWentToFuriaEvent(true);
        interactionModel.setBoughtItems(true);
        interactionModel.setEFuriaClubMember(true);
        interactionModel.setId(interactionId);

        UserInfoModel user = new UserInfoModel();

        // Mock saving interaction
        when(interactionRepository.save(interactionModel)).thenReturn(interactionModel);
        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(user));

        Long resultId = interactionService.createInteractionService(interactionModel, userId);

        assertEquals(interactionId, resultId);
        assertEquals(interactionModel, user.getInteraction());

        verify(fanScoreService, times(1)).updateFanScore(user);
        verify(userInfoRepository, times(1)).findById(userId);
        verify(interactionRepository, times(1)).save(interactionModel);
    }
}
