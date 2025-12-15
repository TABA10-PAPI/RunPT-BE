package com.runpt.back.user.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddUserRequestDto {
    @NotNull
    private Long uid;
    
    @NotNull
    private int age;
    
    @NotNull
    private int height;
    
    @NotNull
    private int weight;
}
