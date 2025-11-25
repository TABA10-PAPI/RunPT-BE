package com.runpt.back.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.runpt.back.community.dto.request.PostRequestDto;

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
    
    private Long uid;
    private String title;
    private String startpoint;
    private int distance;
    private String starttime;
    private String targetpace;
    private String targetgender;
    private String shortinfo;

    private String nickname;
    private String tier;


    @CreationTimestamp
    private LocalDateTime createAt;

    public CommunityEntity(PostRequestDto dto, LocalDateTime time, String nickname, String tier){
        this.uid = dto.getUid();
        this.title = dto.getTitle();
        this.startpoint = dto.getStartpoint();
        this.distance = dto.getDistance();
        this.starttime = dto.getStarttime();
        this.targetpace = dto.getTargetpace();
        this.targetgender = dto.getTargetgender();
        this.shortinfo = dto.getShortinfo();
        this.createAt = time;
        this.nickname = nickname;
        this.tier = tier;
    }
}
