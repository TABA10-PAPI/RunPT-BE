package com.runpt.back.community.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.runpt.back.community.dto.response.*;
import com.runpt.back.community.dto.request.*;
import com.runpt.back.community.service.CommunityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/add")
    public ResponseEntity<? super PostResponseDto> post (
        @RequestBody @Valid PostRequestDto requsetBody
    ){
        ResponseEntity<? super PostResponseDto> response = communityService.post(requsetBody);
        return response;
    }
    
    @PostMapping("/home")
    public ResponseEntity<? super HomeResponseDto> home(
        @RequestBody HomeRequestDto requestBody) {

        ResponseEntity<? super HomeResponseDto> response = communityService.getList(requestBody);

    return ResponseEntity.ok(response);
    }

    @PostMapping("/detail")
    public ResponseEntity<? super DetailResponseDto> detail(
            @RequestBody @Valid DetailRequestDto requestBody) {

        ResponseEntity<? super DetailResponseDto> response = communityService.getDetail(requestBody);

        return response;
    }
     
    @PostMapping("/comment")
    public ResponseEntity<? super CommentResponseDto> comment(
            @RequestBody @Valid CommentRequestDto requestBody
    ) {

        ResponseEntity<? super CommentResponseDto> response =
                communityService.writeComment(requestBody);

        return response;
    }
    
    @PostMapping("/delete")
    public ResponseEntity<? super DeleteResponseDto> delete(
            @RequestBody @Valid DeleteRequestDto requestBody
    ) {

        ResponseEntity<? super DeleteResponseDto> response =
                communityService.delete(requestBody);

        return response;
    }

    @PostMapping("/modify")
    public ResponseEntity<? super ModifyResponseDto> modify(
            @RequestBody @Valid ModifyRequestDto requestBody
    ) {

        ResponseEntity<? super ModifyResponseDto> response =
                communityService.modify(requestBody);

        return response;
    }

    
}