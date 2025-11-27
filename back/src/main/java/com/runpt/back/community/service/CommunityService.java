package com.runpt.back.community.service;

import org.springframework.http.ResponseEntity;
import com.runpt.back.community.dto.request.*;
import com.runpt.back.community.dto.response.*;

public interface CommunityService{
    ResponseEntity<? super PostResponseDto> post(PostRequestDto dto);
    ResponseEntity<? super CommunityHomeResponseDto> getList(CommunityHomeRequestDto dto);
    ResponseEntity<? super DetailResponseDto> getDetail(DetailRequestDto dto);
    ResponseEntity<? super CommentResponseDto> writeComment(CommentRequestDto dto);
    ResponseEntity<? super DeleteResponseDto> delete(DeleteRequestDto dto);
    ResponseEntity<? super ModifyResponseDto> modify(ModifyRequestDto dto);
    ResponseEntity<? super ParticipateResponseDto> participate(ParticipateRequestDto dto);

}
