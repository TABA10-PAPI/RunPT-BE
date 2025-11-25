package com.runpt.back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.runpt.back.user.dto.request.*;
import com.runpt.back.user.dto.response.*;
import com.runpt.back.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // 🔥 카카오 로그인 + 자동 회원가입 통합 API
    @PostMapping("/kakao-login")
    public ResponseEntity<? super KakaoLoginResponseDto> kakaoAuth(
            @RequestBody KakaoLoginRequestDto requestBody
    ) {
        return userService.kakaoLogin(requestBody);
    }

    @PostMapping("/join")
    public ResponseEntity<? super JoinResponseDto> join(
        @RequestBody JoinRequestDto requestBody
    ) { 
        return userService.join(requestBody);
    }
    
    @PostMapping("mypage")
    public ResponseEntity<? super GetMyPageResponseDto> getMyPage(
        @RequestBody GetMyPageRequestDto requestBody
    ) {
        return userService.getMyPage(requestBody);
    }

    @PostMapping("/saeve-running")
    public ResponseEntity<? super SaveRunningResponseDto> saveRunning(
        @RequestBody SaveRunningRequestDto requestBody
    ) {
        return userService.saveRunning(requestBody);
    }
    
}
