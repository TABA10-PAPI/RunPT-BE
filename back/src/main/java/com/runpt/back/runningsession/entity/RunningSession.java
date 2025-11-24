package com.runpt.back.runningsession.entity;

import com.runpt.back.user.entity.UserEntity;
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
public class RunningSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private UserEntity user;

    @Column(name = "distance_m", nullable = false)
    private Integer distanceM; // 미터 단위

    @Column(name = "duration_sec", nullable = false)
    private Integer durationSec; // 초 단위

    @Column(name = "heart_rate_avg")
    private Integer heartRateAvg;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 거리 타입 계산 (3KM, 5KM, 10KM, HALF, FULL)
     */
    public String getDistanceType() {
        double distanceKm = distanceM / 1000.0;
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

    /**
     * 페이스 계산 (분/km 단위)
     */
    public Double getPace() {
        if (distanceM == null || distanceM == 0 || durationSec == null || durationSec == 0) {
            return null;
        }
        double distanceKm = distanceM / 1000.0;
        return (durationSec / 60.0) / distanceKm;
    }
}

