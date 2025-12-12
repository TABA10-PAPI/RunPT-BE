package com.runpt.back.community.repository;

import com.runpt.back.community.entity.ParticipateEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<ParticipateEntity, Long>{
    ParticipateEntity findByUser_IdAndCommunity_Id(Long uid, Long communityid);
    void deleteAllByCommunity_Id(Long communityid);
    List<ParticipateEntity> findByCommunity_Id(Long communityid);

}
