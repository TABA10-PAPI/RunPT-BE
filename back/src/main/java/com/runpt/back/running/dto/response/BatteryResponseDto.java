package com.runpt.back.running.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.running.common.RunningResponseCode;
import com.runpt.back.running.common.RunningResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BatteryResponseDto extends ResponseDto {

    // 명세서에 "battery_score" 라고 되어 있으면 이렇게 매핑
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

        @JsonProperty("target_pace")   // 혹은 target_pace 라고 오면 거기에 맞춰 수정
        private String paceMinPerKm;

        @JsonProperty("reason")
        private String note;              // 코멘트(reason)
    }

    public BatteryResponseDto(float battery, List<RecommendationDto> recommendations) {
        super();
        this.battery = battery;
        this.recommendations = recommendations;
    }

    public static ResponseEntity<BatteryResponseDto> success(float battery, List<RecommendationDto> recommendations) {
        return ResponseEntity.ok(new BatteryResponseDto(battery, recommendations));
    }

     public static ResponseEntity<ResponseDto> batteryNotFound() {
        ResponseDto responseBody = new ResponseDto(RunningResponseCode.BATTERY_NOT_FOUND, RunningResponseMessage.BATTERY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> recommendationParseError() {
        ResponseDto responseBody = new ResponseDto(RunningResponseCode.RECOMMENDATION_PARSE_ERROR, RunningResponseMessage.RECOMMENDATION_PARSE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
