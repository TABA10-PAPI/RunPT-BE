package com.runpt.back.community.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.runpt.back.community.dto.request.CommentRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long communityid;
    private Long uid;
    private String content;

    @CreationTimestamp
    private LocalDateTime createAt;
    
    public CommentEntity(CommentRequestDto dto, LocalDateTime time) {
    this.communityid = dto.getCommunityid();
    this.uid = dto.getUid();
    this.content = dto.getContent();
    this.createAt = time;
    }
}
