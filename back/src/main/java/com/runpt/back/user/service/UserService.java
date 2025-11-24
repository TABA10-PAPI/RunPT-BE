package com.runpt.back.user.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.user.dto.request.KakaoLoginRequestDto;
import com.runpt.back.user.dto.response.*;

public interface UserService {
    ResponseEntity<? super KakaoLoginResponseDto> kakaoLogin(KakaoLoginRequestDto dto);

}