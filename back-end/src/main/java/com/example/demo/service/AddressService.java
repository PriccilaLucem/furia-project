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
        validateAddress(addressModel);
        UserInfoModel user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid user id"));
        
        if (user.getAddress() != null && user.getAddress().getId() != null) {
            throw new RuntimeException("Usuário já possui um endereço cadastrado. Não é permitido cadastrar mais de um.");
        }
        
        addressModel.setId(addressRepository.save(addressModel).getId());
        user.setAddress(addressModel);
        userInfoRepository.save(user);
        return addressModel.getId();
    }
    public void validateAddress(AddressModel addressModel) {
        if (addressModel.getCity() == null || addressModel.getCity().isEmpty()) {
            throw new RuntimeException("City cannot be null or empty");
        }
        if (addressModel.getState() == null || addressModel.getState().isEmpty()) {
            throw new RuntimeException("State cannot be null or empty");
        }
        if (addressModel.getZip() == null || addressModel.getZip().isEmpty()) {
            throw new RuntimeException("Zip code cannot be null or empty");
        }
        if (addressModel.getCountry() == null || addressModel.getCountry().isEmpty()) {
            throw new RuntimeException("Street cannot be null or empty");
        }
    }
}