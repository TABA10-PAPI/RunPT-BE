package com.runpt.back.battery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.runpt.back.battery.dto.request.*;
import com.runpt.back.battery.dto.response.*;
import com.runpt.back.battery.service.BatteryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/battery")
public class BatteryController {

    private final BatteryService batteryService;

    //battery
    @PostMapping("")
    public ResponseEntity<? super BatteryResponseDto> getBattery(
            @RequestBody @Valid BatteryRequestDto requestBody
    ) {
        return batteryService.getBattery(requestBody);
    }
}
