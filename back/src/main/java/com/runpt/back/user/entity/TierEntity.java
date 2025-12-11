package com.runpt.back.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "3km")
    private String km3; // "BRONZE", "SILVER", "GOLD", etc.

    @Column(name = "5km")
    private String km5; 

    @Column(name = "10km")
    private String km10;

    @Column(name = "Half")
    private String half;

    @Column(name = "Full")
    private String full;

    private LocalDateTime updatedAt;

    public TierEntity(UserEntity user) {
        this.user = user;
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
