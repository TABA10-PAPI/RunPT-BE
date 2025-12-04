package com.runpt.back.community.dto.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.community.common.*;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class CommentDeleteResponseDto extends ResponseDto{
    CommentDeleteResponseDto(){
        super();
    }

    public static ResponseEntity<CommentDeleteResponseDto> success(){
        CommentDeleteResponseDto responseBody = new CommentDeleteResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> uidNotExist() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.UID_NOT_EXIST, communityResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.INVALID_ID, communityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> commenNotFound() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.COMMENT_NOT_FOUND, communityResponseMessage.COMMENT_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    
}
