package com.example.demo.repository;

import com.example.demo.model.UserInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoModel, UUID> {
    Optional<UserInfoModel> findByEmail(String username);
}
