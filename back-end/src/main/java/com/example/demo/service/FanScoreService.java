package com.example.demo.service;

import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FanScoreService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public int updateFanScore(UserInfoModel user) {
        int score = 0;

        if (user.getSocialMedia() != null) {
            if (Boolean.TRUE.equals(user.getSocialMedia().getFollowingFuria())) {
                score += 20;
            }
            if (user.getSocialMedia().getTwitterHandle() != null) {
                score += 5;
            }
            if (user.getSocialMedia().getInstagramHandle() != null) {
                score += 5;
            }
            if (user.getSocialMedia().getTiktokHandle() != null) {
                score += 5;
            }
        }

        if (user.getInteraction() != null) {
            if (Boolean.TRUE.equals(user.getInteraction().getAlreadyWentToFuriaEvent())) {
                score += 30;
            }
            if (Boolean.TRUE.equals(user.getInteraction().getBoughtItems())) {
                score += 15;
            }
            if (Boolean.TRUE.equals(user.getInteraction().getEFuriaClubMember())) {
                score += 25;
            }
        }

        user.setFansScore(score);
        userInfoRepository.save(user);

        return score;
    }
}
