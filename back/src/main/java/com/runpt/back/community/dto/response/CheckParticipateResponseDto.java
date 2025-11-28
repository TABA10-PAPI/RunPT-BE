package com.runpt.back.community.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.community.entity.ParticipateEntity;
import com.runpt.back.global.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckParticipateResponseDto extends ResponseDto{
    List<ParticipateEntity> participates;

    public static ResponseEntity<? super CheckParticipateResponseDto> success(List<ParticipateEntity> participates){
        CheckParticipateResponseDto responseBody = new CheckParticipateResponseDto(participates);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
