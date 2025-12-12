package com.runpt.back.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runpt.back.community.entity.CommentEntity;

public interface CommentRepository  extends JpaRepository<CommentEntity, Long>{
    List<CommentEntity> findByCommunity_IdOrderByCreateAtAsc(Long communityid);
    void deleteAllByCommunity_Id(Long communityid);
    CommentEntity findByUser_IdAndCommunity_Id(Long uid, Long Communityid);
    void deleteAllByUser_IdAndCommunity_Id(Long uid, Long Communityid);

    long countByCommunity_Id(Long communityid);
}
