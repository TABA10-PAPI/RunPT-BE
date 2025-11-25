package com.runpt.back.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RunningToAIRequestDto {
    private long user_id;
    private String date;
    private int distance;
    private String pace;
    private int time_sec;
    private int avg_hr;
}
