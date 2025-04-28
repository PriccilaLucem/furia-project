package com.example.demo.repository;

import com.example.demo.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, UUID> {
    Optional<AdminModel> findByEmail(String email);
}
