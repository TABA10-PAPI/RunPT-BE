package com.runpt.back.user.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.common.UserResponseCode;
import com.runpt.back.user.common.UserResponseMessage;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.user.entity.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetMyPageResponseDto extends ResponseDto{
    
    private UserInfo user;
    private TierInfo tier;
    private List<RunningSessionInfo> recentRecords;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String oauthProvider;
        private String oauthUid;
        private String nickname;
        private Integer age;
        private Integer height;
        private Integer weight;
        private String gender;

        public UserInfo(UserEntity user) {
            if (user != null) {
                this.id = user.getId();
                this.oauthProvider = user.getOauthProvider();
                this.oauthUid = user.getOauthUid();
                this.nickname = user.getNickname();
                this.age = user.getAge();
                this.height = user.getHeight();
                this.weight = user.getWeight();
                this.gender = user.getGender();
            }
        }
    }

    @Getter
    @Setter
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
    @Setter
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

    public GetMyPageResponseDto(UserEntity user, TierEntity tier, List<RunningSessionEntity> records) {
        super();
        this.user = new UserInfo(user);
        this.tier = tier != null ? new TierInfo(tier) : null;
        this.recentRecords = records != null ? 
            records.stream().map(RunningSessionInfo::new).toList() : null;
    }

    public static ResponseEntity<GetMyPageResponseDto> getMyPageSuccess(UserEntity user, TierEntity tier, List<RunningSessionEntity> records) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetMyPageResponseDto(user, tier, records));
    }

    public static ResponseEntity<ResponseDto> userNotExists() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.USER_NOT_EXISTS, UserResponseMessage.USER_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
