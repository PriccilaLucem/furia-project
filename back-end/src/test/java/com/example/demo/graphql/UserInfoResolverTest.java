package com.example.demo.graphql;

import com.example.demo.model.*;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoResolverTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoResolver userInfoResolver;

    @Test
    void testUserInfoQuery() {
        // Setup test data
        UserInfoModel user = createTestUser();

        // Correct mocking - no argument matchers needed for findAll()
        when(userInfoRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Test without filters
        List<UserInfoModel> result = userInfoResolver.userInfo(null, null, null, null, null, null);
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());

        // Test with filters
        result = userInfoResolver.userInfo(5, 15, "TestCity", true, null, null);
        assertEquals(1, result.size());

        // Test with non-matching filter
        result = userInfoResolver.userInfo(20, 30, null, null, null, null);
        assertTrue(result.isEmpty());
    }

    private UserInfoModel createTestUser() {
        UserInfoModel user = new UserInfoModel();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("123456789");
        user.setFansScore(10);

        SocialMediaModel socialMedia = new SocialMediaModel();
        socialMedia.setTwitterHandle("@test");
        user.setSocialMedia(socialMedia);

        InteractionModel interaction = new InteractionModel();
        interaction.setAlreadyWentToFuriaEvent(true);
        interaction.setBoughtItems(false);
        interaction.setEFuriaClubMember(true);
        interaction.setUserInfo(user);
        user.setInteraction(interaction);

        AddressModel address = new AddressModel();
        address.setCity("TestCity");
        address.setState("TS");
        address.setZip("00000-000");
        address.setCountry("TestCountry");
        address.setUser(user);
        user.setAddress(address);

        return user;
    }
}