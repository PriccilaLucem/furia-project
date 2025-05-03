package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.UserInfoRepository;

@RestController
@RequestMapping("/api/v1/user/get-all")
public class userInfoGetController {
    @Autowired
    private UserInfoRepository userInfoRepository;



    /**
     * Retrieves a list of users based on the provided filters.
     *
     * @param fansScoreMin Minimum fans score (inclusive).
     * @param fansScoreMax Maximum fans score (inclusive).
     * @param city         City name to filter users by.
     * @param alreadyWentToFuriaEvent Filter for users who have already attended a Furia event.
     * @param boughtItems  Filter for users who have purchased items.
     * @param eFuriaClubMember Filter for users who are members of the eFuria Club.
     * @return A list of users matching the specified filters.
     */
    @GetMapping
    public ResponseEntity<List<UserInfoModel>> getAllUsers(
        @RequestParam(required = false) Integer fansScoreMin,
        @RequestParam(required = false) Integer fansScoreMax,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) Boolean alreadyWentToFuriaEvent,
        @RequestParam(required = false) Boolean boughtItems,
        @RequestParam(required = false) Boolean eFuriaClubMember
    ) {
        List<UserInfoModel> users = userInfoRepository.findWithFilters(
            fansScoreMin, fansScoreMax, city, state, alreadyWentToFuriaEvent, boughtItems, eFuriaClubMember
        );
        return ResponseEntity.ok(users);
    }
}
