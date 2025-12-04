package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.common.UserResponseCode;
import com.runpt.back.user.common.UserResponseMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SaveRunningResponseDto extends ResponseDto{
    
    public static ResponseEntity<SaveRunningResponseDto> saveRunningSuccess() {
        return ResponseEntity.status(HttpStatus.OK).body(new SaveRunningResponseDto());
    }

    public static ResponseEntity<ResponseDto> invalidRunningData() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.INVALID_RUNNING_DATA, UserResponseMessage.INVALID_RUNNING_DATA);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidDateFormat() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.INVALID_DATE_FORMAT, UserResponseMessage.INVALID_DATE_FORMAT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> tierCalculationError() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.TIER_CALCULATION_ERROR, UserResponseMessage.TIER_CALCULATION_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> runningSaveFailed() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.RUNNING_SAVE_FAILED, UserResponseMessage.RUNNING_SAVE_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> tierSaveFailed() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.TIER_SAvE_FAILED, UserResponseMessage.TIER_SAvE_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExists() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.USER_NOT_EXISTS, UserResponseMessage.USER_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
