package com.runpt.back.mypage.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OverallTierResponse {
    private Long uid;
    private String highestTier; 
    private String highestTierKorean; 
    private Map<String, String> tierByDistanceType;
}

