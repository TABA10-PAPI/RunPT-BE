package com.runpt.back.community.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.community.entity.ParticipateEntity;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.community.common.CommunityResponseCode;
import com.runpt.back.community.common.CommunityResponseMessage;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckParticipateResponseDto extends ResponseDto{
    
    private List<ParticipateInfo> participates;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ParticipateInfo {
        private Long id;
        private Long uid;
        private Long communityid;
        private String nickname;
        private String tier;

        public ParticipateInfo(ParticipateEntity participate, String tier) {
            if (participate != null) {
                this.id = participate.getId();
                this.uid = participate.getUser() != null ? participate.getUser().getId() : null;
                this.communityid = participate.getCommunity() != null ? participate.getCommunity().getId() : null;
                this.nickname = participate.getUser() != null ? participate.getUser().getNickname() : null;
                this.tier = tier;
            }
        }
    }

    public CheckParticipateResponseDto(List<ParticipateInfo> participates) {
        super();
        this.participates = participates;
    }

    public static ResponseEntity<? super CheckParticipateResponseDto> success(List<ParticipateInfo> participates){
        CheckParticipateResponseDto responseBody = new CheckParticipateResponseDto(participates);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.INVALID_ID, CommunityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }   
}
