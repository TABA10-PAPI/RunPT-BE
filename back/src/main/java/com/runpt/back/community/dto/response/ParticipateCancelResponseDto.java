package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

public class ParticipateCancelResponseDto extends ResponseDto{
    ParticipateCancelResponseDto(){
        super();
    }

    public static ResponseEntity<ParticipateCancelResponseDto> success(){
        ParticipateCancelResponseDto responseBody = new ParticipateCancelResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
