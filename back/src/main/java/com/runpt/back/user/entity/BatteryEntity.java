package com.runpt.back.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "battery")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    private String date;

    private String feedback;

    private String reason;

    private float battery;

    @Column(columnDefinition = "TEXT")
    private String recommendationsJson;
}
