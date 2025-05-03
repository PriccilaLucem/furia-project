package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InteractionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Boolean alreadyWentToFuriaEvent;
    Boolean boughtItems;
    Boolean eFuriaClubMember;

    @JsonIgnore
    @OneToOne(mappedBy = "interaction", fetch = FetchType.LAZY)
    private UserInfoModel userInfo;
}
