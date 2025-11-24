package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class SaveRunningResponseDto extends ResponseDto{
    
    public static ResponseEntity<SaveRunningResponseDto> saveRunningSuccess() {
        return ResponseEntity.status(HttpStatus.OK).body(new SaveRunningResponseDto());
    }
}
