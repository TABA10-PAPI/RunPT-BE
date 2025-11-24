package com.runpt.back.community.service;

import org.springframework.http.ResponseEntity;
import com.runpt.back.community.dto.request.*;
import com.runpt.back.community.dto.response.*;

public interface CommunityService{
    ResponseEntity<? super PostResponseDto> post(PostRequestDto dto);
    ResponseEntity<? super HomeResponseDto> getList(HomeRequestDto dto);
    ResponseEntity<? super DetailResponseDto> getDetail(DetailRequestDto dto);
    ResponseEntity<? super CommentResponseDto> writeComment(CommentRequestDto dto);


}
