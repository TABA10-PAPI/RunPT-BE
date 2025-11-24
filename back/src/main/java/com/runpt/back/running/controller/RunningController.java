package com.runpt.back.running.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.runpt.back.running.dto.request.RunningRequestDto;
import com.runpt.back.running.dto.response.RunningResponseDto;
import com.runpt.back.running.service.RunningService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/running")
public class RunningController {

    private final RunningService runningService;

    // POST /running
    @PostMapping("")
    public ResponseEntity<? super RunningResponseDto> getRunningPlan(
            @RequestBody @Valid RunningRequestDto requestBody
    ) {
        return runningService.getRunningPlan(requestBody);
    }
}
