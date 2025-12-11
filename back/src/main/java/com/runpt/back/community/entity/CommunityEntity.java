package com.runpt.back.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.runpt.back.community.dto.request.*;
import com.runpt.back.user.entity.UserEntity;


import java.time.LocalDateTime;

@Entity
@Table(name = "community")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false)
    private UserEntity user;
    private String title;
    private String startpoint;
    private int distance;
    private String starttime;
    private String targetpace;
    private String targetgender;
    private String shortinfo;
    private String tier; // 가장 높은 티어 저장

    private int participateuser;

    private Long commentCount;

    @CreationTimestamp
    private LocalDateTime createAt;

    public CommunityEntity(PostRequestDto dto, LocalDateTime time, UserEntity user, String tier){
        this.user = user;
        this.title = dto.getTitle();
        this.startpoint = dto.getStartpoint();
        this.distance = dto.getDistance();
        this.starttime = dto.getStarttime();
        this.targetpace = dto.getTargetpace();
        this.targetgender = dto.getTargetgender();
        this.shortinfo = dto.getShortinfo();
        this.tier = tier;
        this.participateuser = 0;
        this.createAt = time;
    }

    public void update(ModifyRequestDto dto){
        this.title = dto.getTitle();
        this.startpoint = dto.getStartpoint();
        this.distance = dto.getDistance();
        this.starttime = dto.getStarttime();
        this.targetpace = dto.getTargetpace();
        this.targetgender = dto.getTargetgender();
        this.shortinfo = dto.getShortinfo();
    }

    public void increaseParticipant(){
        this.participateuser += 1;
    }

    public void decreaseParticipant(){
        this.participateuser -= 1;
    }
}
