package com.runpt.back.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.runpt.back.ai.dto.request.AiBatteryRequestDto;
import com.runpt.back.ai.service.AiService;
import com.runpt.back.battery.dto.response.BatteryResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/battery")
    public ResponseEntity<? super BatteryResponseDto> analyzeBattery(
            @RequestBody AiBatteryRequestDto requestBody
    ) {
        return aiService.analyzeBattery(requestBody);
    }
}
