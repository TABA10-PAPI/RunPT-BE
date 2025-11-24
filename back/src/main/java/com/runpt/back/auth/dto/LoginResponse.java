package com.runpt.back.auth.dto;

import com.runpt.back.user.entity.User;
import lombok.Builder;
import lombok.Getter;

/**
 * 카카오 OAuth 로그인 시 프론트로 내려가는 응답 DTO
 * - uid (우리 서버의 user ID)
 * - accessToken
 * - refreshToken
 * - isNewUser (신규 가입 여부)
 */
@Getter
@Builder
public class LoginResponse {

    private Long uid;
    private boolean isNewUser;

    public static LoginResponse of(User user, boolean isNewUser) {
        return LoginResponse.builder()
                .uid(user.getId())
                .isNewUser(isNewUser)
                .build();
    }
}