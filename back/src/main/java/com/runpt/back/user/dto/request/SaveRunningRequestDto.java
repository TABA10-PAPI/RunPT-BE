package com.runpt.back.user.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveRunningRequestDto {
    private long uid;
    private LocalDateTime date;
    private int pace; // 1km 당 초 단위
    private int distance; // 미터 단위
    private int durationSec; // 초 단위
    private int heartRateAvg;
}
