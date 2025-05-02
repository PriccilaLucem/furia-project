package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String password;
    String name;
    @Column(unique = true)
    String email;
    String phone;

    private Integer fansScore = 0;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userInfo")
    private SocialMediaModel socialMedia;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userInfo")
    private InteractionModel interaction;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userInfo")
    private AddressModel address;
}
