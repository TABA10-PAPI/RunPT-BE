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

    @Column(name = "3km")
    private String km3; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "5km")
    private String km5; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "10km")
    private String km10;

    @Column(name = "Half")
    private String half;

    @Column(name = "Full")
    private String full;

    private LocalDateTime updatedAt;

    public TierEntity(long uid) {
        this.uid = uid;
        this.km3 = null;
        this.km5 = null;
        this.km10 = null;
        this.half = null;
        this.full = null;
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
