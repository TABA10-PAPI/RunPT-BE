package com.runpt.back.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runpt.back.user.entity.TierEntity;

public interface TierRepository extends JpaRepository<TierEntity, Long> {
    TierEntity findByUid(long uid);
}
