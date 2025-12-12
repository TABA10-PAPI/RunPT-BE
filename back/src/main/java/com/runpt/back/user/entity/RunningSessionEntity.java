package com.runpt.back.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "running_session",
       uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "date"}))
public class RunningSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private int pace; // 1km 당 초 단위

    @Column(name = "distance_m", nullable = false)
    private int distance; // 미터 단위

    @Column(name = "duration_sec", nullable = false)
    private int durationSec; // 초 단위

    @Column(name = "heart_rate_avg")
    private int heartRateAvg;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}


