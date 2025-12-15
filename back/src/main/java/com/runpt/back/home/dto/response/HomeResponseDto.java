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
    private BatteryInfo battery;
    private TierInfo tier;
    private List<RunningSessionInfo> runningSession;

    @Getter
    @NoArgsConstructor
    public static class BatteryInfo {
        private Long id;
        private Long uid;
        private String date;
        private String feedback;
        private String reason;
        private float battery;
        private String recommendationsJson;

        public BatteryInfo(BatteryEntity battery) {
            if (battery != null) {
                this.id = battery.getId();
                this.uid = battery.getUser() != null ? battery.getUser().getId() : null;
                this.date = battery.getDate();
                this.feedback = battery.getFeedback();
                this.reason = battery.getReason();
                this.battery = battery.getBattery();
                this.recommendationsJson = battery.getRecommendationsJson();
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class TierInfo {
        private Long id;
        private Long uid;
        private String km3;
        private String km5;
        private String km10;
        private String half;
        private String full;
        private java.time.LocalDateTime updatedAt;

        public TierInfo(TierEntity tier) {
            if (tier != null) {
                this.id = tier.getId();
                this.uid = tier.getUser() != null ? tier.getUser().getId() : null;
                this.km3 = tier.getKm3();
                this.km5 = tier.getKm5();
                this.km10 = tier.getKm10();
                this.half = tier.getHalf();
                this.full = tier.getFull();
                this.updatedAt = tier.getUpdatedAt();
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RunningSessionInfo {
        private Long id;
        private Long uid;
        private int pace;
        private int distance;
        private int durationSec;
        private int heartRateAvg;
        private java.time.LocalDateTime date;

        public RunningSessionInfo(RunningSessionEntity session) {
            if (session != null) {
                this.id = session.getId();
                this.uid = session.getUser() != null ? session.getUser().getId() : null;
                this.pace = session.getPace();
                this.distance = session.getDistance();
                this.durationSec = session.getDurationSec();
                this.heartRateAvg = session.getHeartRateAvg();
                this.date = session.getDate();
            }
        }
    }

    public HomeResponseDto(Long uid,
                           String date,
                           String nickname,
                           BatteryEntity battery,
                           TierEntity tier,
                           List<RunningSessionEntity> runningSession) {

        this.uid = uid;
        this.date = date;
        this.nickname = nickname;
        this.battery = battery != null ? new BatteryInfo(battery) : null;
        this.tier = tier != null ? new TierInfo(tier) : null;
        this.runningSession = runningSession != null ? 
            runningSession.stream().map(RunningSessionInfo::new).toList() : null;
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
