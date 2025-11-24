package com.runpt.back.mypage.controller;

import com.runpt.back.mypage.dto.MyPageHomeResponse;
import com.runpt.back.mypage.dto.OverallTierResponse;
import com.runpt.back.mypage.dto.RunningRecordResponse;
import com.runpt.back.mypage.dto.UidRequest;
import com.runpt.back.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 1. 유저의 종합 티어 계산 및 반환
     * POST /mypage/tier
     */
    @PostMapping("/tier")
    public ResponseEntity<OverallTierResponse> getOverallTier(
            @RequestBody UidRequest request
    ) {
        OverallTierResponse response = myPageService.getOverallTier(request.getUid());
        return ResponseEntity.ok(response);
    }

    /**
     * 2. 지난 러닝기록 조회
     * POST /mypage/history
     */
    @PostMapping("/history")
    public ResponseEntity<List<RunningRecordResponse>> getRunningHistory(
            @RequestBody UidRequest request
    ) {
        List<RunningRecordResponse> response = myPageService.getRunningHistory(request.getUid());
        return ResponseEntity.ok(response);
    }

    /**
     * 3. 마이페이지 홈
     * POST /mypage/home
     */
    @PostMapping("/home")
    public ResponseEntity<MyPageHomeResponse> getMyPageHome(
            @RequestBody UidRequest request
    ) {
        MyPageHomeResponse response = myPageService.getMyPageHome(request.getUid());
        return ResponseEntity.ok(response);
    }
}

