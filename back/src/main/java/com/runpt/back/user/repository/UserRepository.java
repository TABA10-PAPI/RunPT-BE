package com.runpt.back.user.repository;

import com.runpt.back.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByOauthProviderAndOauthUid(String oauthProvider, String oauthUid);
    UserEntity findById(long uid);
}