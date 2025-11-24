package com.runpt.back.running.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.running.dto.request.RunningRequestDto;
import com.runpt.back.running.dto.response.RunningResponseDto;

public interface RunningService {

    ResponseEntity<? super RunningResponseDto> getRunningPlan(RunningRequestDto dto);
}

