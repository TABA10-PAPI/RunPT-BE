package com.runpt.back.home.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeRecommendationDto {

    private String type;

    @JsonProperty("distance_km")
    private String distanceKm;

    @JsonProperty("target_pace")
    private String paceMinPerKm;

    @JsonProperty("reason")
    private String note;
}

