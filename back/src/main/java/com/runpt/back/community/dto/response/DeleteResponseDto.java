package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

public class DeleteResponseDto extends ResponseDto{
    
    DeleteResponseDto(){
        super();
    }

    public static ResponseEntity<DeleteResponseDto> success(){
        DeleteResponseDto responseBody = new DeleteResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
