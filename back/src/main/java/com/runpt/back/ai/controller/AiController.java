package com.runpt.back.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.runpt.back.ai.dto.request.AiBatteryRequestDto;
import com.runpt.back.ai.service.AiService;

import jakarta.validation.Valid;

import com.runpt.back.ai.dto.response.AiBatteryResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/battery")
    public ResponseEntity<? super AiBatteryResponseDto> analyzeBattery(
            @RequestBody @Valid AiBatteryRequestDto requestBody
    ) {
        ResponseEntity<? super AiBatteryResponseDto> response = aiService.analyzeBattery(requestBody);
        return response;
    }
}
