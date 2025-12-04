package com.runpt.back.community.dto.response;

import java.time.LocalDateTime;
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
public class DetailResponseDto extends ResponseDto{
    private Long id;           // 엔티티의 PK 필드명에 맞게 수정 필요
    private Long uid;
    private String nickname;
    private String tier;
    private String title;
    private String startpoint;
    private int distance;
    private String starttime;
    private String targetpace;
    private String targetgender;
    private String shortinfo;
    private LocalDateTime createAt;
    private int participateuser;

    private List<CommunityCommentResponseDto> comments;

    public DetailResponseDto(CommunityEntity community,  List<CommunityCommentResponseDto> comments) {
        this.id = community.getId();              
        this.uid = community.getUid();
        this.nickname = community.getNickname();
        this.tier = community.getTier();
        this.title = community.getTitle();
        this.startpoint = community.getStartpoint();
        this.distance = community.getDistance();
        this.starttime = community.getStarttime();
        this.targetpace = community.getTargetpace();
        this.targetgender = community.getTargetgender();
        this.shortinfo = community.getShortinfo();
        this.createAt = community.getCreateAt();
        this.participateuser = community.getParticipateuser();

        this.comments = comments;
    }

    public static ResponseEntity<? super DetailResponseDto> success(CommunityEntity community, List<CommunityCommentResponseDto> comments) {
        DetailResponseDto responseBody = new DetailResponseDto(community, comments);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> communityNotFound() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.COMMUNITY_NOT_FOUND, CommunityResponseMessage.COMMUNITY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidId() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.INVALID_ID, CommunityResponseMessage.INVALID_ID);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> commentNotFound() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.COMMENT_NOT_FOUND, CommunityResponseMessage.COMMENT_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userNotExist() {
        ResponseDto responseBody = new ResponseDto(CommunityResponseCode.USER_NOT_EXIST, CommunityResponseMessage.USER_NOT_EXIST);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
    
}
