package com.runpt.back.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runpt.back.user.entity.BatteryEntity;

public interface BatteryRepository extends JpaRepository<BatteryEntity, Long> {

    Optional<BatteryEntity> findByUidAndDate(Long uid, String date);
    BatteryEntity findByUid(Long uid);
}
