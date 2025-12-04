package com.runpt.back.global.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.common.ResponseCode;
import com.runpt.back.global.common.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private String code;
    private String message;

    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> validationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> badRequest() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.BAD_REQUEST, ResponseMessage.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}