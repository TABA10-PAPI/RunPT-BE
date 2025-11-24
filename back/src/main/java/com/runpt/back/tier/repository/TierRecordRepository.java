package com.runpt.back.tier.repository;

import com.runpt.back.tier.entity.TierRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TierRecordRepository extends JpaRepository<TierRecord, Long> {
    Optional<TierRecord> findByUserId(Long uid);
}

