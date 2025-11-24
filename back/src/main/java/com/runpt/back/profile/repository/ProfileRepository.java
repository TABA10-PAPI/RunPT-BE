package com.runpt.back.profile.repository;

import com.runpt.back.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    java.util.Optional<Profile> findByUserId(Long uid);
}