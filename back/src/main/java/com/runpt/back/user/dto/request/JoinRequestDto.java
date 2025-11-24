package com.runpt.back.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequestDto {
    @NotBlank
    private long uid;
    @NotBlank
    private String nickname;
    @NotBlank
    private int age;
    @NotBlank
    private int height;
    @NotBlank
    private int weight;
}
