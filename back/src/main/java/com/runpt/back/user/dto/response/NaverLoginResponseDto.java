package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.common.ResponseCode;
import com.runpt.back.global.common.ResponseMessage;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.common.UserResponseCode;
import com.runpt.back.user.common.UserResponseMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NaverLoginResponseDto extends ResponseDto{
    private long uid;
    private boolean fresh;
    private String nickname;
    public NaverLoginResponseDto(long uid, boolean fresh, String nickname) {
        super();
        this.uid = uid;
        this.fresh = fresh;
        this.nickname = nickname;
    }

    public static ResponseEntity<NaverLoginResponseDto> naverLoginSuccess(long uid, boolean fresh, String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(new NaverLoginResponseDto(uid, fresh, nickname));
    }

    public static ResponseEntity<ResponseDto> invalidAccessToken() {
        ResponseDto responsebody = new ResponseDto(UserResponseCode.INVALID_ACCESS_TOKEN, UserResponseMessage.INVALID_ACCESS_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responsebody);
}

    public static ResponseEntity<ResponseDto> oauthApiError() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.OAUTH_API_ERROR, UserResponseMessage.OAUTH_API_ERROR);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
