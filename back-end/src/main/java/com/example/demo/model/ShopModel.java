package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ShopModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserInfoModel user;

    private Double valor;
    private LocalDate dataCompra;
}
