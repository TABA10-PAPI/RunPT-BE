package com.runpt.back.running.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.running.common.RunningResponseCode;
import com.runpt.back.running.common.RunningResponseMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BatteryResponseDto extends ResponseDto {

    @JsonProperty("battery_score")
    private float battery;

    private String feedback;
    private String reason;

    private List<RunningRecommendationDto> recommendations;

    public BatteryResponseDto(float battery, String feedback, String reason,
                              List<RunningRecommendationDto> recommendations) {
        super();
        this.battery = battery;
        this.feedback = feedback;
        this.reason = reason;
        this.recommendations = recommendations;
    }

    public static ResponseEntity<BatteryResponseDto> success(float battery,
                                                             String feedback,
                                                             String reason,
                                                             List<RunningRecommendationDto> recommendations) {
        return ResponseEntity.ok(new BatteryResponseDto(battery, feedback, reason, recommendations));
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
