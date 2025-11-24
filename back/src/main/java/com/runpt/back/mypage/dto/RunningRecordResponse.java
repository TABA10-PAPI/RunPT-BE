package com.runpt.back.mypage.dto;

import com.runpt.back.runningsession.entity.RunningSession;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RunningRecordResponse {
    private Long id;
    private Integer distanceM; 
    private Integer durationSec; 
    private Double pace; 
    private String distanceType;
    private LocalDateTime date;
    private String tier; 

    public static RunningRecordResponse from(RunningSession session, String tier) {
        return RunningRecordResponse.builder()
                .id(session.getId())
                .distanceM(session.getDistanceM())
                .durationSec(session.getDurationSec())
                .pace(session.getPace())
                .distanceType(session.getDistanceType())
                .date(session.getDate())
                .tier(tier)
                .build();
    }
}

