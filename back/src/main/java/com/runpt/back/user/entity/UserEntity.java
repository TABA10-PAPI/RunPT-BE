package com.runpt.back.user.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @Column(nullable = false)
    private String oauthProvider;   

    @Column(nullable = false)
    private String oauthUid;
    
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = true)
    private Integer height;

    @Column(nullable = true)
    private Integer weight;

    @Column(length = 1, nullable = true)
    private String gender;
}