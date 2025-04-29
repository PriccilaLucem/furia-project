package com.example.demo.graphql;

import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserInfoResolver {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @QueryMapping
    public List<UserInfoModel> userInfo(Integer fansScoreMin, Integer fansScoreMax, String city, Boolean alreadyWentToFuriaEvent, Boolean boughtItems, Boolean eFuriaClubMember) {
        List<UserInfoModel> allUsers = userInfoRepository.findAll();

        return allUsers.stream()
                .filter(user -> {
                    boolean matches = true;

                    if (fansScoreMin != null) {
                        matches &= user.getFansScore() != null && user.getFansScore() >= fansScoreMin;
                    }
                    if (fansScoreMax != null) {
                        matches &= user.getFansScore() != null && user.getFansScore() <= fansScoreMax;
                    }
                    if (city != null) {
                        matches &= user.getAddresses().stream().anyMatch(address -> city.equalsIgnoreCase(address.getCity()));
                    }
                    if (alreadyWentToFuriaEvent != null) {
                        matches &= user.getInteraction() != null && alreadyWentToFuriaEvent.equals(user.getInteraction().getAlreadyWentToFuriaEvent());
                    }
                    if (boughtItems != null) {
                        matches &= user.getInteraction() != null && boughtItems.equals(user.getInteraction().getBoughtItems());
                    }
                    if (eFuriaClubMember != null) {
                        matches &= user.getInteraction() != null && eFuriaClubMember.equals(user.getInteraction().getEFuriaClubMember());
                    }

                    return matches;
                })
                .collect(Collectors.toList());
    }
}
