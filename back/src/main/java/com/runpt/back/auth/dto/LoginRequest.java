package com.runpt.back.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String code;   // 카카오 인가코드
}