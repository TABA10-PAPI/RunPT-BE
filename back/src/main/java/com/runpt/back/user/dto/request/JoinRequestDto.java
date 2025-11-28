package com.runpt.back.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequestDto {
    @NotNull
    private long uid;
    @NotBlank
    private String nickname;
    @NotNull
    private int age;
    @NotNull
    private int height;
    @NotNull
    private int weight;
    @NotNull
    private String gender;
}
