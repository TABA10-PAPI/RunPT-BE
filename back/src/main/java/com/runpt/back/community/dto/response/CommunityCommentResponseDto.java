package com.runpt.back.community.dto.response;

import java.time.LocalDateTime;

import com.runpt.back.community.entity.CommentEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityCommentResponseDto {

    private Long id;
    private Long communityid;
    private Long uid;
    private String content;
    private LocalDateTime createAt;

    private String nickname;
    private String tier;

    public CommunityCommentResponseDto(CommentEntity comment , String nickname, String tier) {
        this.id = comment.getId();
        this.communityid = comment.getCommunityid();
        this.uid = comment.getUid();
        this.content = comment.getContent();
        this.createAt = comment.getCreateAt();
        this.nickname = nickname;
        this.tier = tier;
    }
}
