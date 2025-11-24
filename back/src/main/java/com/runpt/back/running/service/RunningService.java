package com.runpt.back.running.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.running.dto.request.*;
import com.runpt.back.running.dto.response.*;

public interface RunningService {

    ResponseEntity<? super RunningResponseDto> getRunningPlan(RunningRequestDto dto);
    ResponseEntity<? super BatteryResponseDto> getBattery(BatteryRequestDto dto);
}

