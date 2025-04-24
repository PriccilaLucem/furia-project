package com.example.demo.service;

import com.example.demo.model.AddressModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public Long save(AddressModel addressModel, UUID userId) {
        addressModel.setId(addressRepository.save(addressModel).getId());
        UserInfoModel user = userInfoRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user id"));
        user.addAddress(addressModel);
        userInfoRepository.save(user);
        return addressModel.getId();
    }

}
