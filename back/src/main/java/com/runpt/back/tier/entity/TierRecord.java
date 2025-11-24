package com.runpt.back.tier.entity;

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
@Table(name = "tier_record")
public class TierRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "short_tier_rank")
    private String shortTierRank; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "long_tier_rank")
    private String longTierRank; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "short_best_time")
    private Integer shortBestTime; // 초 단위

    @Column(name = "long_best_time")
    private Integer longBestTime; // 초 단위

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

