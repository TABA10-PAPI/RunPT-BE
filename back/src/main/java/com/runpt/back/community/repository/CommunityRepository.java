package com.runpt.back.community.repository;

import com.runpt.back.community.entity.CommunityEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long>{
    
    List<CommunityEntity> findByTargetgenderInOrderByCreateAtDesc(List<String> genders);
    long deleteByUidAndId(Long uid, Long id);  
    boolean existsByUidAndId(Long uid, Long id);
}
