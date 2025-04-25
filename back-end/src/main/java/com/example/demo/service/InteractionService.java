package com.example.demo.service;


import com.example.demo.model.InteractionModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.InteractionRepository;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FanScoreService fanScoreService;

    public Long createInteractionService(InteractionModel interactionModel, UUID userId) {
        InteractionModel userInteraction =  interactionRepository.save(interactionModel);
        UserInfoModel userInfo = userInfoRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));
        userInfo.setInteraction(userInteraction);
        fanScoreService.updateFanScore(userInfo);
        return interactionModel.getId();
    }
}
