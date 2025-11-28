package com.runpt.back.user.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.user.dto.request.*;
import com.runpt.back.user.dto.response.*;

public interface UserService {
    ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto);
   //ResponseEntity<? super NaverLoginResponseDto> naverLogin(NaverLoginRequestDto dto);
    ResponseEntity<? super JoinResponseDto> join(JoinRequestDto dto);
    ResponseEntity<? super GetMyPageResponseDto> getMyPage(GetMyPageRequestDto dto);
    ResponseEntity<? super SaveRunningResponseDto> saveRunning(SaveRunningRequestDto dto);
}