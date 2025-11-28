package com.runpt.back.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverLoginRequestDto {
    private String accessToken;
    private String date;
}
