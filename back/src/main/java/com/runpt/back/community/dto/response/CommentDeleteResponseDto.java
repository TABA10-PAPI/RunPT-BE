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
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.UID_NOT_EXIST, CommunityResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.INVALID_ID, CommunityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> commenNotFound() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.COMMENT_NOT_FOUND, CommunityResponseMessage.COMMENT_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    
}
