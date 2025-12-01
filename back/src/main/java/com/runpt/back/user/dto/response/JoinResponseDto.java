package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.common.UserResponseCode;
import com.runpt.back.user.common.UserResponseMessage;
import com.runpt.back.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponseDto extends ResponseDto{
    UserEntity user;

    public static ResponseEntity<JoinResponseDto> joinSuccess(UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(new JoinResponseDto(user));
    }

    public static ResponseEntity<ResponseDto> userNotExitsts() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.USER_NOT_EXISTS, UserResponseMessage.USER_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
