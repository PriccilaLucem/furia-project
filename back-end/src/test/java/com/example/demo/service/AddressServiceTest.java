package com.example.demo.service;

import com.example.demo.model.AddressModel;
import com.example.demo.model.UserInfoModel;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void shouldSaveAddressAndReturnId() {
        UUID userId = UUID.randomUUID();
        Long generatedAddressId = 10L;

        AddressModel address = new AddressModel();
        address.setCity("Rio de Janeiro");
        address.setState("RJ");
        address.setZip("20000-000");
        address.setCountry("Brasil");

        AddressModel savedAddress = new AddressModel();
        savedAddress.setId(generatedAddressId);

        UserInfoModel user = new UserInfoModel();
        user.setId(userId);

        when(addressRepository.save(address)).thenReturn(savedAddress);
        when(userInfoRepository.findById(userId)).thenReturn(Optional.of(user));

        Long resultId = addressService.save(address, userId);

        assertEquals(generatedAddressId, resultId);
        verify(addressRepository).save(address);
        verify(userInfoRepository).findById(userId);
        verify(userInfoRepository).save(user);
    }
    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        AddressModel address = new AddressModel();
        address.setCity("Rio de Janeiro");
        address.setState("RJ");
        address.setZip("20000-000");
        address.setCountry("Brasil");
        
        when(userInfoRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> 
            addressService.save(address, userId),
            "Invalid user id");
    }
}