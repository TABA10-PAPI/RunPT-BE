package com.runpt.back.running.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RunningRequestDto {

    // JSON으로는 "user_id" 로 받고, 서버 내부에서는 uid로 사용
    @JsonProperty("user_id")
    private Long uid;

    // "2025-11-21" 이런 형식
    private String date;
}
