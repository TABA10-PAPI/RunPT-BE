package com.runpt.back.community.repository;

import com.runpt.back.community.entity.ParticipateEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<ParticipateEntity, Long>{
    ParticipateEntity findByUidAndCommunityid(Long uid, Long communityid);
    void deleteAllByCommunityid(Long communityid);
    List<ParticipateEntity> findByCommunityid(Long communityid);

}
