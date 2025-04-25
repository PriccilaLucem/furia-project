package com.example.demo.service;

import com.example.demo.model.InteractionModel;
import com.example.demo.model.SocialMediaModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FanScoreServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private FanScoreService fanScoreService;

    @Test
    void shouldCalculateFanScoreCorrectly() {
        UserInfoModel user = new UserInfoModel();

        SocialMediaModel social = new SocialMediaModel();
        social.setFollowingFuria(true);
        social.setTwitterHandle("@test");
        social.setInstagramHandle("@insta");
        social.setTiktokHandle("@tiktok");

        InteractionModel interaction = new InteractionModel();
        interaction.setAlreadyWentToFuriaEvent(true);
        interaction.setBoughtItems(true);
        interaction.setEFuriaClubMember(true);

        user.setSocialMedia(social);
        user.setInteraction(interaction);

        // Evita salvar de verdade, apenas simula
        Mockito.when(userInfoRepository.save(Mockito.any(UserInfoModel.class))).thenReturn(user);

        int score = fanScoreService.updateFanScore(user);

        // 20 (following) + 5 (twitter) + 5 (insta) + 5 (tiktok)
        // + 30 (event) + 15 (items) + 25 (club) = 105
        assertEquals(105, score);
        assertEquals(105, user.getFansScore());
    }
}
