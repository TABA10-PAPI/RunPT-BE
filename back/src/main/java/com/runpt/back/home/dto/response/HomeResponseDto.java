package com.runpt.back.home.dto.response;

import com.runpt.back.global.dto.ResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeResponseDto extends ResponseDto {

    private Long uid;
    private String date;
    private String nickname;
    private float battery;
    private HomeRecommendationDto recommendation;
    private int distance;
    private String tier;

    public HomeResponseDto(Long uid,
                           String date,
                           String nickname,
                           float battery,
                           HomeRecommendationDto recommendation,
                           int distance,
                           String tier) {

        this.uid = uid;
        this.date = date;
        this.nickname = nickname;
        this.battery = battery;
        this.recommendation = recommendation;
        this.distance = distance;
        this.tier = tier;
    }

    public static ResponseEntity<? super HomeResponseDto> success(
           Long uid,
           String date,
           String nickname,
           float battery,
           HomeRecommendationDto recommendation,
           int distance,
           String tier) {

        HomeResponseDto responseBody =
            new HomeResponseDto(uid, date, nickname, battery, recommendation, distance, tier);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
