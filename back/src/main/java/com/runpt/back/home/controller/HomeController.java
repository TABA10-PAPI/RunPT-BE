package com.runpt.back.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.runpt.back.home.dto.request.HomeRequestDto;
import com.runpt.back.home.dto.response.HomeResponseDto;
import com.runpt.back.home.service.HomeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    @PostMapping("")
    public ResponseEntity<? super HomeResponseDto> getHome(
            @Valid @RequestBody HomeRequestDto requestBody) {
        return homeService.getHome(requestBody);
    }
}
