package com.example.demo.service;

import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.util.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public UUID createUser(UserInfoModel userInfoModel) {
        validateUser(userInfoModel);
        userInfoModel.setPassword(Authorization.hashPassword(userInfoModel.getPassword()));
        return userInfoRepository.save(userInfoModel).getId();
    }

    public UserInfoModel getUserInfo(UUID id) {
        return userInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserInfoModel getByEmail(String email) {
        return userInfoRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserInfoModel> getAllUsers() {
        return userInfoRepository.findAll();
    }

    public void validateUser(UserInfoModel userInfoModel) {
        if(userInfoModel.getName() == null || userInfoModel.getName().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if(userInfoModel.getPassword() == null || userInfoModel.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        if(userInfoModel.getEmail() == null || userInfoModel.getEmail().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }
        if(userInfoModel.getPhone() == null || userInfoModel.getPhone().isEmpty()) {
            throw new RuntimeException("Phone cannot be empty");
        }
        if (!userInfoModel.getPhone().matches("^(\\+55\\s?)?\\(?\\d{2}\\)?\\s?(9\\d{4}|\\d{4})-?\\d{4}$")) {
            throw new RuntimeException("Invalid phone number");
        }
        if (!userInfoModel.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            throw new RuntimeException("Invalid email");
        }
    }
}
