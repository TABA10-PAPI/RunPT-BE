package com.runpt.back.running.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.runpt.back.running.common.RunningResponseMessage;
import com.runpt.back.running.common.RunningResponseCode;
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
        ResponseDto responseBody = new ResponseDto(RunningResponseCode.UID_NOT_EXISTS, RunningResponseMessage.UID_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> dateNotExists() {
        ResponseDto responseBody = new ResponseDto(RunningResponseCode.DATE_NOT_EXISTS, RunningResponseMessage.DATE_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
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
