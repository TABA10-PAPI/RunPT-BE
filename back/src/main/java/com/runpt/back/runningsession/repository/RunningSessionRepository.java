package com.runpt.back.runningsession.repository;

import com.runpt.back.runningsession.entity.RunningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningSessionRepository extends JpaRepository<RunningSession, Long> {
    
    @Query("SELECT r FROM RunningSession r WHERE r.user.id = :uid ORDER BY r.date DESC")
    List<RunningSession> findByUserIdOrderByDateDesc(@Param("uid") Long uid);
    
    @Query("SELECT r FROM RunningSession r WHERE r.user.id = :uid ORDER BY r.date DESC")
    List<RunningSession> findRecentRunningSessionsByUserId(@Param("uid") Long uid, org.springframework.data.domain.Pageable pageable);
}

