package com.runpt.back.community.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.community.entity.CommunityEntity;
import com.runpt.back.global.dto.ResponseDto;

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
}
