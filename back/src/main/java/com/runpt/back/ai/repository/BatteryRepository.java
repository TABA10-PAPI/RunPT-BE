package com.runpt.back.ai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runpt.back.ai.entity.BatteryEntity;

public interface BatteryRepository extends JpaRepository<BatteryEntity, Long> {

    Optional<BatteryEntity> findByUidAndDate(Long uid, String date);
}
