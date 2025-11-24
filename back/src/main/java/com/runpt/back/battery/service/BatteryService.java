package com.runpt.back.battery.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.battery.dto.request.*;
import com.runpt.back.battery.dto.response.*;



public interface BatteryService {
    ResponseEntity<? super BatteryResponseDto> getBattery(BatteryRequestDto dto);

}
