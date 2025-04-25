package com.example.demo.repository;

import com.example.demo.model.InteractionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionRepository extends JpaRepository<InteractionModel, Long> {
}
