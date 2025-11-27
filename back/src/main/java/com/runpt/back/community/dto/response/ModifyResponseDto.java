package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

import lombok.Getter;

@Getter
public class ModifyResponseDto extends ResponseDto{
    ModifyResponseDto(){
        super();
    }

    public static ResponseEntity<ModifyResponseDto> success(){
        ModifyResponseDto responseBody = new ModifyResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
