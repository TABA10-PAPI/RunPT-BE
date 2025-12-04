package com.runpt.back.community.entity;

import com.runpt.back.community.dto.request.ParticipateRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participate")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long communityid;
    private Long uid;
    private String nickname;
    private String tier;

    public ParticipateEntity(ParticipateRequestDto dto, String nickname, String tier){
        this.communityid = dto.getCommunityid();
        this.uid = dto.getUid();
        this.nickname = nickname;
        this.tier = tier;
    }
}
