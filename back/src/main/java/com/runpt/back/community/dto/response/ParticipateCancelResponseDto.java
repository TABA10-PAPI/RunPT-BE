package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.community.common.*;

public class ParticipateCancelResponseDto extends ResponseDto{
    ParticipateCancelResponseDto(){
        super();
    }

    public static ResponseEntity<ParticipateCancelResponseDto> success(){
        ParticipateCancelResponseDto responseBody = new ParticipateCancelResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExist() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.UID_NOT_EXIST, communityResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> communityNotFound() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.COMMUNITY_NOT_FOUND, communityResponseMessage.COMMUNITY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }  

    public static ResponseEntity<ResponseDto> notParticipated() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.NOT_PARTICIPATED, communityResponseMessage.NOT_PARTICIPATED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.INVALID_ID, communityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
