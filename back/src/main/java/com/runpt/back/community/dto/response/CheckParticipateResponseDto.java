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

        public ParticipateInfo(ParticipateEntity participate) {
            if (participate != null) {
                this.id = participate.getId();
                this.uid = participate.getUser() != null ? participate.getUser().getId() : null;
                this.communityid = participate.getCommunity() != null ? participate.getCommunity().getId() : null;
                this.nickname = participate.getCommunity() != null && participate.getCommunity().getUser() != null 
                    ? participate.getCommunity().getUser().getNickname() : null;
                this.tier = participate.getCommunity() != null ? participate.getCommunity().getTier() : null;
            }
        }
    }

    public CheckParticipateResponseDto(List<ParticipateEntity> participates) {
        super();
        this.participates = participates != null ? 
            participates.stream().map(ParticipateInfo::new).toList() : null;
    }

    public static ResponseEntity<? super CheckParticipateResponseDto> success(List<ParticipateEntity> participates){
        CheckParticipateResponseDto responseBody = new CheckParticipateResponseDto(participates);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.INVALID_ID, CommunityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }   
}
