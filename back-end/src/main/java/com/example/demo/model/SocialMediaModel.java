package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "social_media")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SocialMediaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String twitterHandle;
    String instagramHandle;
    String tiktokHandle;
    String twitchUsername;
    String steamUsername;
    String riotUsername;
    String plataformasFavoritas;
    Boolean seguindoFuriaNasRedes;
    String interacoesRecentes;
}
