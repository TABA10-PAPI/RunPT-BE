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
}
