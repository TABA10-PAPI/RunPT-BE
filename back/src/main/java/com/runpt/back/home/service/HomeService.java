package com.runpt.back.home.service;

import org.springframework.http.ResponseEntity;

import com.runpt.back.home.dto.request.HomeRequestDto;
import com.runpt.back.home.dto.response.HomeResponseDto;

public interface HomeService {
    ResponseEntity<? super HomeResponseDto> getHome(HomeRequestDto dto);
}
