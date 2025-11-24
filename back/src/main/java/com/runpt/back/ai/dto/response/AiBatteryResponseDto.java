package com.runpt.back.ai.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runpt.back.global.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiBatteryResponseDto extends ResponseDto{

    @JsonProperty("battery_score")
    private float battery;

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

    AiBatteryResponseDto(float battery, List<RecommendationDto> recommendations){
        super();
        this.battery = battery;
        this.recommendations = recommendations;
    }

    public static ResponseEntity<AiBatteryResponseDto> success(float battery, List<RecommendationDto> recommendations){
        AiBatteryResponseDto responseBody = new AiBatteryResponseDto(battery, recommendations);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
