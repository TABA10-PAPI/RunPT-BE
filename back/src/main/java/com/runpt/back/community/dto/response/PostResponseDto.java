package com.runpt.back.community.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.community.common.communityResponseCode;
import com.runpt.back.community.common.communityResponseMessage;
import com.runpt.back.global.dto.ResponseDto;

import lombok.Getter;


@Getter
public class PostResponseDto extends ResponseDto{

    PostResponseDto(){
        super();
    }
    //http 통신의 통신상태를 알려주는 코드 - 그냥 규격이라고 생각하면 편할 듯
    public static ResponseEntity<PostResponseDto> success(){
        PostResponseDto responseBody = new PostResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExist() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.USER_NOT_EXIST, communityResponseMessage.USER_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExist() {
        ResponseDto responseBody = new ResponseDto(communityResponseCode.UID_NOT_EXIST, communityResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
