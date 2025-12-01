package com.runpt.back.community.dto.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;

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
}
