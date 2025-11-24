package com.runpt.back.profile.entity;

import com.runpt.back.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user; 

    @Column(nullable = false)
    private String name;
    private Integer age;
    private Integer height;
    private Integer weight;
    private String region;

    @Column(length = 1)
    private String gender; // m or f

    private Double shortAverageSpeed;
    private Double longAverageSpeed;

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}