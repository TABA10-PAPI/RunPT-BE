package com.runpt.back.auth.controller;

import com.runpt.back.auth.dto.LoginResponse;
import com.runpt.back.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoService kakaoService;

    /**
     * 카카오 OAuth redirect_uri 에서 code 받아 로그인 처리
     * 예: GET /auth/kakao?code=xxxxx
     */
    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {

        LoginResponse response = kakaoService.login(code);

        return ResponseEntity.ok(response);
    }
}