package com.example.demo.controller;

import com.example.demo.model.AddressModel;
import com.example.demo.config.security.JwtService;
import com.example.demo.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {


    @Mock
    private AddressService addressService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AddressController addressController;

    @Test
    void shouldPostAddressSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        Long addressId = 123L;
        String token = "Bearer fake-jwt-token";

        AddressModel address = new AddressModel();
        address.setCity("Cidade");
        address.setState("Estado");
        address.setZip("00000-000");

        Mockito.when(jwtService.extractUserId(Mockito.anyString())).thenReturn(userId);
        Mockito.when(addressService.save(Mockito.any(AddressModel.class), Mockito.any(UUID.class))).thenReturn(addressId);

        ResponseEntity<?> response = addressController.postAddress(address, token);


        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(addressId, body.get("id"));
        assertEquals("success", body.get("message"));
    }
}
