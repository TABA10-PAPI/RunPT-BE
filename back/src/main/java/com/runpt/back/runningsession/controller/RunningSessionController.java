package com.runpt.back.runningsession.controller;

import com.runpt.back.runningsession.dto.RunningSessionRequest;
import com.runpt.back.runningsession.entity.RunningSession;
import com.runpt.back.runningsession.service.RunningSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/running-session")
@RequiredArgsConstructor
public class RunningSessionController {

    private final RunningSessionService runningSessionService;

    /**
     * 러닝 세션 저장
     * POST /running-session
     */
    @PostMapping
    public ResponseEntity<RunningSession> saveRunningSession(
            @RequestBody RunningSessionRequest request
    ) {
        RunningSession session = RunningSession.builder()
                .distanceM(request.getDistanceM())
                .durationSec(request.getDurationSec())
                .heartRateAvg(request.getHeartRateAvg())
                .date(request.getDate())
                .build();

        RunningSession saved = runningSessionService.saveRunningSession(request.getUid(), session);
        return ResponseEntity.ok(saved);
    }
}

