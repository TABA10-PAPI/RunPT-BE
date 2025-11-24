package com.runpt.back.profile.dto;

import com.runpt.back.profile.entity.Profile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private Long uid;
    private String name;
    private Integer age;
    private Integer height;
    private Integer weight;
    private String region;
    private String gender;
    private String highestTier; 
    private String highestTierKorean; 

    public static ProfileResponse from(Profile p, String highestTier, String highestTierKorean) {
        return ProfileResponse.builder()
                .uid(p.getUser().getId())
                .name(p.getName())
                .age(p.getAge())
                .height(p.getHeight())
                .weight(p.getWeight())
                .region(p.getRegion())
                .gender(p.getGender())
                .highestTier(highestTier)
                .highestTierKorean(highestTierKorean)
                .build();
    }
}