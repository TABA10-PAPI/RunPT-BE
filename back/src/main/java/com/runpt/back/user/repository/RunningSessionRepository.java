package com.runpt.back.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runpt.back.user.entity.RunningSessionEntity;

public interface RunningSessionRepository extends JpaRepository<RunningSessionEntity, Long> {
    List<RunningSessionEntity> findAllByUid(long uid);
    List<RunningSessionEntity> findByUidOrderByDateDesc(long uid);
}