package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

public class ParticipateResponseDto extends ResponseDto{
    ParticipateResponseDto(){
        super();
    }

    public static ResponseEntity<ParticipateResponseDto> success(){
        ParticipateResponseDto responseBody = new ParticipateResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
