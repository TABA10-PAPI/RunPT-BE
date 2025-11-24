package com.runpt.back.ai.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.ai.dto.request.AiBatteryRequestDto;
import com.runpt.back.battery.dto.response.BatteryResponseDto;

public interface AiService {

    ResponseEntity<? super BatteryResponseDto> analyzeBattery(AiBatteryRequestDto dto);
}
