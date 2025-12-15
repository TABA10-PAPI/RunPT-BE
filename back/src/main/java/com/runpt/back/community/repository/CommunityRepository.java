package com.runpt.back.community.repository;

import com.runpt.back.community.entity.CommunityEntity;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long>{
    List<CommunityEntity> findByTargetgenderInOrderByCreateAtDesc(List<String> genders);
    void existsByUser_IdAndId(Long uid, Long id);
    
    @EntityGraph(attributePaths = {"user"})
    List<CommunityEntity> findByTargetgenderOrTargetgenderOrderByCreateAtDesc(String gender1, String gender2);
    
}
