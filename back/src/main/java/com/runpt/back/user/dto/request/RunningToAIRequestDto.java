package com.runpt.back.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class RunningToAIRequestDto {

    @JsonProperty("user_id")
    private int userId;

    private String date;
    private double distance;

    @JsonProperty("pace_sec")
    private int paceSec;

    @JsonProperty("time_sec")
    private int timeSec;

    @JsonProperty("avg_hr")
    private int avgHr;

    public RunningToAIRequestDto(long userId, String date, double distance, int paceSec, int timeSec, int avgHr) {
        this.userId = (int)userId;
        this.date = date;
        this.distance = (double)distance;
        this.paceSec = paceSec;
        this.timeSec = timeSec;
        this.avgHr = avgHr;
    }
}
