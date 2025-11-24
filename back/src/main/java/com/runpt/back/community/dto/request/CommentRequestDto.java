package com.runpt.back.community.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @NotNull
    private Long communityid;

    @NotNull
    private Long uid;

    @NotNull
    private String content;
}
