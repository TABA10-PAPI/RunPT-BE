package com.runpt.back.ai.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.ai.dto.request.AiBatteryRequestDto;
import com.runpt.back.ai.dto.response.AiBatteryResponseDto;

public interface AiService {
    ResponseEntity<? super AiBatteryResponseDto> analyzeBattery(AiBatteryRequestDto dto);
}
