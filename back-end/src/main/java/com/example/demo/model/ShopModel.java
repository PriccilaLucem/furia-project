package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserInfoModel user;

    private Double valor;
    private LocalDate shopDate;
}
