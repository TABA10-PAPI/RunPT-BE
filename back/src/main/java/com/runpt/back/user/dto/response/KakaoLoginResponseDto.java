package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLoginResponseDto extends ResponseDto{

    private long uid;
    private boolean isNew;
    private String nickname;
    public KakaoLoginResponseDto(long uid, boolean isNew, String nickname) {
        super();
        this.uid = uid;
        this.isNew = isNew;
        this.nickname = nickname;
    }

    public static ResponseEntity<KakaoLoginResponseDto> kakaoLoginSuccess(long uid, boolean isNew, String nickname) {
        return ResponseEntity.status(HttpStatus.OK).body(new KakaoLoginResponseDto(uid, isNew, nickname));
    }    
}
