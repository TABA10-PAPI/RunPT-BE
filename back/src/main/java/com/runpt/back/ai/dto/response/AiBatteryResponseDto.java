package com.runpt.back.ai.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiBatteryResponseDto {

    @JsonProperty("battery_score")
    private float battery;

    private float stress;

    private List<RecommendationDto> recommendations;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationDto {

        private String type;

        @JsonProperty("distance_km")
        private String distanceKm;

        @JsonProperty("target_pace")
        private String paceMinPerKm;

        @JsonProperty("reason")
        private String note;
    }
}
