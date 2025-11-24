package com.runpt.back.ai.entity;

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

    private Long uid;

    private String date;

    private float battery;

    @Column(columnDefinition = "TEXT")
    private String recommendationsJson;
}
