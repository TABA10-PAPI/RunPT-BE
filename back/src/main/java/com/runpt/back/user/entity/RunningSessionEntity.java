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
@Table(name = "running_session")
public class RunningSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private Long uid;

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

    /**
     * 거리 타입 계산 (3KM, 5KM, 10KM, HALF, FULL)
     */
    public String getDistanceType() {
        double distanceKm = distance / 1000.0;
        if (distanceKm <= 3.5) {
            return "3KM";
        } else if (distanceKm <= 5.5) {
            return "5KM";
        } else if (distanceKm <= 10.5) {
            return "10KM";
        } else if (distanceKm <= 22.0) {
            return "HALF";
        } else {
            return "FULL";
        }
    }
}


