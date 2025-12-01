package com.runpt.back.community.dto.request;

import lombok.Getter;

@Getter
public class CommentDeleteRequestDto {
    private Long uid;
    private Long communityid;
}
