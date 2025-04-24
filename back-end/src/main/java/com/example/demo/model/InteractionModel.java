package com.example.demo.model;

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
    Boolean BoughtItems;
    Boolean eFuriaClubMember;
    Integer memberLevel;

    @OneToOne
    private UserInfoModel userInfo;
}
