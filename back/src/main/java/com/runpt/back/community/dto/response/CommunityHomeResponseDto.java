package com.runpt.back.community.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.community.entity.CommunityEntity;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.community.common.CommunityResponseCode;
import com.runpt.back.community.common.CommunityResponseMessage;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommunityHomeResponseDto extends ResponseDto{
    
    private List<CommunityInfo> communitys;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CommunityInfo {
        private Long id;
        private Long uid;
        private String nickname;
        private String gender;
        private String title;
        private String startpoint;
        private int distance;
        private String starttime;
        private String targetpace;
        private String targetgender;
        private String shortinfo;
        private String tier;
        private int participateuser;
        private long commentCount;
        private java.time.LocalDateTime createAt;

        public CommunityInfo(CommunityEntity community) {
            if (community != null) {
                this.id = community.getId();
                this.uid = community.getUser() != null ? community.getUser().getId() : null;
                this.nickname = community.getUser() != null ? community.getUser().getNickname() : null;
                this.gender = community.getUser() != null ? community.getUser().getGender() : null;
                this.title = community.getTitle();
                this.startpoint = community.getStartpoint();
                this.distance = community.getDistance();
                this.starttime = community.getStarttime();
                this.targetpace = community.getTargetpace();
                this.targetgender = community.getTargetgender();
                this.shortinfo = community.getShortinfo();
                this.tier = community.getTier();
                this.participateuser = community.getParticipateuser();
                this.commentCount = community.getCommentCount();
                this.createAt = community.getCreateAt();
            }
        }
    }

    public CommunityHomeResponseDto(List<CommunityEntity> entityList) {
        super();
        this.communitys = entityList != null ? 
            entityList.stream().map(CommunityInfo::new).toList() : null;
    }

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
