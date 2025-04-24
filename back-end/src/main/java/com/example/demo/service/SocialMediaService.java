package com.example.demo.service;

import com.example.demo.model.SocialMediaModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.SocialMediaRepository;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SocialMediaService {
    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public Long create(SocialMediaModel socialMediaModel, UUID userId) {
        UserInfoModel userInfoModel = userInfoRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        SocialMediaModel createdSocial = socialMediaRepository.save(socialMediaModel);
        userInfoModel.setSocialMedia(createdSocial);
        userInfoRepository.save(userInfoModel);
        return createdSocial.getId();
    }
}
