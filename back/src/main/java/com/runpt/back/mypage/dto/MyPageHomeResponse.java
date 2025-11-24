package com.runpt.back.mypage.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageHomeResponse {
    
    private Long uid;
    private String name;
    private Integer age;
    private Integer height;
    private Integer weight;
    private String region;
    private String gender;

    private String highestTier;
    private String highestTierKorean;

    private List<RunningRecordResponse> recentRecords;
}

