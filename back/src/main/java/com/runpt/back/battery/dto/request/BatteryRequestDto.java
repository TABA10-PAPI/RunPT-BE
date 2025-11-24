package com.runpt.back.battery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BatteryRequestDto {

    @JsonProperty("user_id")   // JSON: "user_id"
    private Long uid;

    // "2025-11-21" 같은 형식
    private String date;
}