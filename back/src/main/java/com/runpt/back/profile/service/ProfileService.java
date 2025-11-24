package com.runpt.back.profile.service;

import com.runpt.back.mypage.service.MyPageService;
import com.runpt.back.profile.dto.ProfileRequest;
import com.runpt.back.profile.dto.ProfileResponse;
import com.runpt.back.profile.entity.Profile;
import com.runpt.back.profile.repository.ProfileRepository;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final MyPageService myPageService;

    /** 프로필 생성 (회원가입 단계) */
    public Profile createProfile(Long uid, ProfileRequest request) {

        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        Profile profile = Profile.builder()
                .user(user)
                .name(request.getName())
                .age(request.getAge())
                .height(request.getHeight())
                .weight(request.getWeight())
                .gender(request.getGender())
                .region(request.getRegion())
                .build();

        return profileRepository.save(profile);
    }

    /** 프로필 조회 */
    public Profile getProfile(Long uid) {
        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        return profileRepository.findByUserId(uid)
                .orElseThrow(() -> new RuntimeException("프로필이 존재하지 않습니다."));
    }

    /** 프로필 조회 (티어 정보 포함) */
    public ProfileResponse getProfileWithTier(Long uid) {
        Profile profile = getProfile(uid);
        
        // 티어 정보 조회
        var tierResponse = myPageService.getOverallTier(uid);
        
        return ProfileResponse.from(
                profile,
                tierResponse.getHighestTier(),
                tierResponse.getHighestTierKorean()
        );
    }

    /** 프로필 수정 */
    public Profile updateProfile(Long uid, ProfileRequest request) {

        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Profile profile = profileRepository.findByUserId(uid)
                .orElseThrow(() -> new RuntimeException("프로필이 존재하지 않습니다."));

        // 수정 가능한 필드 업데이트
        profile.setName(request.getName());
        profile.setAge(request.getAge());
        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());
        profile.setGender(request.getGender());
        profile.setRegion(request.getRegion());

        return profileRepository.save(profile);
    }
}