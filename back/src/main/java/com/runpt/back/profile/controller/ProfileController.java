package com.runpt.back.profile.controller;

import com.runpt.back.mypage.dto.UidRequest;
import com.runpt.back.profile.dto.ProfileRequest;
import com.runpt.back.profile.dto.ProfileResponse;
import com.runpt.back.profile.entity.Profile;
import com.runpt.back.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 회원가입 마지막 단계 — 프로필 생성
     * POST /profile
     */
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @RequestBody ProfileRequest request
    ) {
        Profile profile = profileService.createProfile(request.getUid(), request);
       
        ProfileResponse response = ProfileResponse.from(
                profile,
                "BRONZE",
                "브론즈"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 내 프로필 조회 (티어 정보 포함)
     * POST /profile/get
     */
    @PostMapping("/get")
    public ResponseEntity<ProfileResponse> getMyProfile(
            @RequestBody UidRequest request
    ) {
        ProfileResponse response = profileService.getProfileWithTier(request.getUid());
        return ResponseEntity.ok(response);
    }

    /**
     * 내 프로필 수정 
     * PUT /profile
     */
    @PutMapping
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @RequestBody ProfileRequest request
    ) {
        profileService.updateProfile(request.getUid(), request);
        
        ProfileResponse response = profileService.getProfileWithTier(request.getUid());
        return ResponseEntity.ok(response);
    }
}