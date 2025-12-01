package com.runpt.back.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runpt.back.community.entity.CommentEntity;

public interface CommentRepository  extends JpaRepository<CommentEntity, Long>{
    List<CommentEntity> findByCommunityidOrderByCreateAtAsc(Long communityid);
    void deleteAllByCommunityid(Long communityid);
    CommentEntity findByUidAndCommunityid(Long uid, Long Communityid);
    void deleteAllByUidAndCommunityid(Long uid, Long Communityid);
}
