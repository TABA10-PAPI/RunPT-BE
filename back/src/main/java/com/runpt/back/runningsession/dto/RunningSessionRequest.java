package com.runpt.back.runningsession.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RunningSessionRequest {
    private Long uid;
    private Integer distanceM; 
    private Integer durationSec; 
    private Integer heartRateAvg;
    private LocalDateTime date;
}

