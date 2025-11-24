package com.runpt.back.runningsession.service;

import com.runpt.back.runningsession.entity.RunningSession;
import com.runpt.back.runningsession.repository.RunningSessionRepository;
import com.runpt.back.tier.service.TierService;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RunningSessionService {

    private final RunningSessionRepository runningSessionRepository;
    private final TierService tierService;
    private final UserRepository userRepository;

    /**
     * 러닝 세션 저장 및 티어 업데이트
     */
    @Transactional
    public RunningSession saveRunningSession(Long uid, RunningSession runningSession) {
        UserEntity user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        runningSession.setUser(user);
        RunningSession saved = runningSessionRepository.save(runningSession);

        // 티어 재계산 및 저장
        tierService.updateTierRecord(uid);

        return saved;
    }
}

