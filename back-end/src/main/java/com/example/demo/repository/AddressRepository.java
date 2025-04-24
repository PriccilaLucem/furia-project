package com.example.demo.repository;

import com.example.demo.model.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository  extends JpaRepository<AddressModel, Long> {
}
