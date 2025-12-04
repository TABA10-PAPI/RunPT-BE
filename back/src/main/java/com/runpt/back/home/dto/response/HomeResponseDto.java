package com.runpt.back.home.dto.response;

import com.runpt.back.global.dto.ResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.runpt.back.home.common.*;

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

    public static ResponseEntity<ResponseDto> uidNotExists() {
        ResponseDto responseBody = new ResponseDto(homeResponseCode.UID_NOT_EXIST, homeResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> dateNotExists() {
        ResponseDto responseBody = new ResponseDto(homeResponseCode.DATE_NOT_EXIST, homeResponseMessage.DATE_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> batteryNotFound() {
        ResponseDto responseBody = new ResponseDto(homeResponseCode.BATTERY_NOT_FOUND, homeResponseMessage.BATTERY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> recommendationParamError() {
        ResponseDto responseBody = new ResponseDto(homeResponseCode.RECOMMENDATRION_PARAM_ERROR, homeResponseMessage.RECOMMENDATRION_PARAM_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExists() {
        ResponseDto responseBody = new ResponseDto(homeResponseCode.USER_NOT_EXIST, homeResponseMessage.USER_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
