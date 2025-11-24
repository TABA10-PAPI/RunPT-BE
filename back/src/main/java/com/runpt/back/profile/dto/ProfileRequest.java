// 프로필 생성/수정 요청
package com.runpt.back.profile.dto;

import lombok.Getter;

@Getter
public class ProfileRequest {
    private Long uid;
    private String name;
    private Integer age;
    private Integer height;
    private Integer weight;
    private String gender;  
    private String region;
}