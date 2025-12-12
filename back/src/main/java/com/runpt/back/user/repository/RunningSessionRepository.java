package com.runpt.back.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.runpt.back.user.entity.RunningSessionEntity;

public interface RunningSessionRepository extends JpaRepository<RunningSessionEntity, Long> {

    // 전체 리스트 조회 (최근순)
    List<RunningSessionEntity> findByUser_IdOrderByDateDesc(Long uid);

    // 가장 최근 1개 조회
    Optional<RunningSessionEntity> findTop1ByUser_IdOrderByDateDesc(Long uid);
    List<RunningSessionEntity> findByUser_IdAndDateBetweenOrderByDateDesc(Long uid, LocalDateTime start, LocalDateTime end);

    // uid와 date로 중복 체크
    Optional<RunningSessionEntity> findByUser_IdAndDate(Long uid, LocalDateTime date);

}
