package com.runpt.back.community.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.community.entity.CommunityEntity;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.community.common.CommunityResponseCode;
import com.runpt.back.community.common.CommunityResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityHomeResponseDto extends ResponseDto{
    List<CommunityEntity> communitys;

    public static ResponseEntity<? super CommunityHomeResponseDto> success(List<CommunityEntity> entityList){
        CommunityHomeResponseDto responseBody = new CommunityHomeResponseDto(entityList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> uidNotExist() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.UID_NOT_EXIST, CommunityResponseMessage.UID_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExist() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.USER_NOT_EXIST, CommunityResponseMessage.USER_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userGenderInvalid() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.USER_GENDER_INVALID, CommunityResponseMessage.USER_GENDER_INVALID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> commentCountError() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.COMMENT_COUNT_ERROR, CommunityResponseMessage.COMMENT_COUNT_ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
