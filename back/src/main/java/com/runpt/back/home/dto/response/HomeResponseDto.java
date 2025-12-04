package com.runpt.back.home.dto.response;

import com.runpt.back.global.dto.ResponseDto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.runpt.back.home.common.HomeResponseMessage;
import com.runpt.back.user.entity.BatteryEntity;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.home.common.HomeResponseCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeResponseDto extends ResponseDto {

    private Long uid;
    private String date;
    private String nickname;
    private BatteryEntity battery;
    private TierEntity tier;
    private List<RunningSessionEntity> runningSession;

    public HomeResponseDto(Long uid,
                           String date,
                           String nickname,
                           BatteryEntity battery,
                           TierEntity tier,
                           List<RunningSessionEntity> runningSession) {

        this.uid = uid;
        this.date = date;
        this.nickname = nickname;
        this.battery = battery;
        this.tier = tier;
        this.runningSession = runningSession;
    }

    public static ResponseEntity<? super HomeResponseDto> success(
           Long uid,
           String date,
           String nickname,
           BatteryEntity battery,
           TierEntity tier,
           List<RunningSessionEntity> runningSession) {

        HomeResponseDto responseBody =
            new HomeResponseDto(uid, date, nickname, battery, tier, runningSession);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExists() {
        ResponseDto responseBody = new ResponseDto(HomeResponseCode.UID_NOT_EXIST, HomeResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> dateNotExists() {
        ResponseDto responseBody = new ResponseDto(HomeResponseCode.DATE_NOT_EXIST, HomeResponseMessage.DATE_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> batteryNotFound() {
        ResponseDto responseBody = new ResponseDto(HomeResponseCode.BATTERY_NOT_FOUND, HomeResponseMessage.BATTERY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> recommendationParamError() {
        ResponseDto responseBody = new ResponseDto(HomeResponseCode.RECOMMENDATRION_PARAM_ERROR, HomeResponseMessage.RECOMMENDATRION_PARAM_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExists() {
        ResponseDto responseBody = new ResponseDto(HomeResponseCode.USER_NOT_EXIST, HomeResponseMessage.USER_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
