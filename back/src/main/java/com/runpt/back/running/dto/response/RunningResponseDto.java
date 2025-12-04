package com.runpt.back.running.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.runpt.back.running.common.runningResponseCode;
import com.runpt.back.running.common.runningResponseMessage;

import com.runpt.back.global.dto.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RunningResponseDto extends ResponseDto {

    private Long uid;
    private String date;
    private List<RunningRecommendationDto> recommendations;

    public RunningResponseDto(Long uid, String date, List<RunningRecommendationDto> recommendations) {
        this.uid = uid;
        this.date = date;
        this.recommendations = recommendations;
    }

    public static ResponseEntity<? super RunningResponseDto> success(
            Long uid,
            String date,
            List<RunningRecommendationDto> recommendations
    ) {
        RunningResponseDto responseBody = new RunningResponseDto(uid, date, recommendations);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExists() {
        ResponseDto responseBody = new ResponseDto(runningResponseCode.UID_NOT_EXISTS, runningResponseMessage.UID_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> dateNotExists() {
        ResponseDto responseBody = new ResponseDto(runningResponseCode.DATE_NOT_EXISTS, runningResponseMessage.DATE_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> batteryNotFound() {
        ResponseDto responseBody = new ResponseDto(runningResponseCode.BATTERY_NOT_FOUND, runningResponseMessage.BATTERY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> recommendationParseError() {
        ResponseDto responseBody = new ResponseDto(runningResponseCode.RECOMMENDATION_PARSE_ERROR, runningResponseMessage.RECOMMENDATION_PARSE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
