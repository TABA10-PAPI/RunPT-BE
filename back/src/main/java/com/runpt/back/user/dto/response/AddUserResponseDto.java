package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.common.UserResponseCode;
import com.runpt.back.user.common.UserResponseMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddUserResponseDto extends ResponseDto {
    
    private String message;

    public AddUserResponseDto(String message) {
        super();
        this.message = message;
    }

    public static ResponseEntity<? super AddUserResponseDto> success() {
        AddUserResponseDto responseBody = new AddUserResponseDto("User information sent to AI successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExist() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.UID_NOT_EXIST, UserResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExists() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.USER_NOT_EXISTS, UserResponseMessage.USER_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
