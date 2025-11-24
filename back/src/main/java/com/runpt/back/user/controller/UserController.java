package com.runpt.back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.response.KakaoLoginResponseDto;
import com.runpt.back.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    // 🔥 카카오 로그인 + 자동 회원가입 통합 API
    @PostMapping("/kakao")
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoAuth(
            @RequestBody KakaoLoginRequestDto requestBody
    ) {
        return userService.kakaoLogin(requestBody);
    }
}
