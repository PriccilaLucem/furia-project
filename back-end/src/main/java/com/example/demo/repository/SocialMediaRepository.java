package com.example.demo.repository;

import com.example.demo.model.SocialMediaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMediaModel, Long> {

}
