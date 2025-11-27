package com.runpt.back.community.dto.request;

import lombok.Getter;

@Getter
public class ModifyRequestDto {
    private Long uid;
    private Long id;
    private String title;
    private String startpoint;
    private int distance;
    private String starttime;
    private String targetpace;
    private String targetgender;
    private String shortinfo;
}
