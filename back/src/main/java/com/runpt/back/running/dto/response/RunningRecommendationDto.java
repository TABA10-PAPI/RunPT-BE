package com.runpt.back.running.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RunningRecommendationDto {

    private String type;

    @JsonProperty("distance_km")
    private String distanceKm;

    @JsonProperty("target_pace")
    private String paceMinPerKm;

    @JsonProperty("reason")
    private String note;
}

