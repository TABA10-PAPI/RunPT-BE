package com.runpt.back.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiBatteryRequestDto {

    @JsonProperty("user_id")
    private Long uid;

    private String date;   // "2025-11-21"
}
