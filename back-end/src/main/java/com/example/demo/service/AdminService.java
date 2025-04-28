package com.example.demo.service;

import com.example.demo.model.AdminModel;
import com.example.demo.repository.AdminRepository;
import com.example.demo.util.Authorization;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public UUID save(AdminModel admin) throws BadRequestException {
        validateAdmin(admin);
        admin.setPassword(Authorization.hashPassword(admin.getPassword()));
        return adminRepository.save(admin).getId();
    }

    public AdminModel findByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
    }

    public void validateAdmin(AdminModel admin) throws BadRequestException{
        if(admin.getPassword() == null || admin.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }
        if (admin.getEmail() == null || !admin.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            throw new BadRequestException("Invalid email");
        }
    }
}
