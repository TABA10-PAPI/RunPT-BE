package com.runpt.back.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tier_record")
public class TierEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private long uid;

    @Column(name = "short_tier_rank")
    private String shortTierRank; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "long_tier_rank")
    private String longTierRank; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "short_best_time")
    private int shortBestTime; // 초 단위

    @Column(name = "long_best_time")
    private int longBestTime; // 초 단위

    private LocalDateTime updatedAt;

    public TierEntity(long uid) {
        this.uid = uid;
        this.shortTierRank = null;
        this.longTierRank = null;
        this.shortBestTime = 0;
        this.longBestTime = 0;
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
