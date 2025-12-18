package com.runpt.back.home.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BatteryToAiRequestDto {
    private long user_id;
    private String date;
}

