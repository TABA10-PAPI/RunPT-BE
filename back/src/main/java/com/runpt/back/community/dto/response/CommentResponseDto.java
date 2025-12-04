package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.community.common.*;

import lombok.Getter;

@Getter
public class CommentResponseDto extends ResponseDto{
    
    CommentResponseDto(){
        super();
    }
    //http 통신의 통신상태를 알려주는 코드 - 그냥 규격이라고 생각하면 편할 듯
    public static ResponseEntity<CommentResponseDto> success(){
        CommentResponseDto responseBody = new CommentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExist() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.UID_NOT_EXIST, CommunityResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.INVALID_ID, CommunityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

}
