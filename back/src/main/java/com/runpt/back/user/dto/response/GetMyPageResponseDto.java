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

@Getter
@Setter
@Getter
@NoArgsConstructor
public class GetMyPageResponseDto extends ResponseDto{
    UserEntity user;
    TierEntity tier;
    List<RunningSessionEntity> recentRecords;

    public GetMyPageResponseDto(UserEntity user, TierEntity tier, List<RunningSessionEntity> records) {
        super();
        this.user = user;
        this.tier = tier;
        this.recentRecords = records;
    }

    public static ResponseEntity<GetMyPageResponseDto> getMyPageSuccess(UserEntity user, TierEntity tier, List<RunningSessionEntity> records) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetMyPageResponseDto(user, tier, records));
    }

    public static ResponseEntity<ResponseDto> userNotExists() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.USER_NOT_EXISTS, UserResponseMessage.USER_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
