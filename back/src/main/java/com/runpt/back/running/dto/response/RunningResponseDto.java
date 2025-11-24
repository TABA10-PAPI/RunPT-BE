package com.runpt.back.running.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
}
