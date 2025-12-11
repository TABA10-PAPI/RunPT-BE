package com.runpt.back.community.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.runpt.back.community.dto.request.CommentRequestDto;
import com.runpt.back.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "communityid", referencedColumnName = "id", nullable = false)
    private CommunityEntity community;
    private String content;

    @CreationTimestamp
    private LocalDateTime createAt;
    
    public CommentEntity(CommentRequestDto dto, LocalDateTime time, UserEntity user, CommunityEntity community) {
        this.user = user;
        this.community = community;
    this.content = dto.getContent();
    this.createAt = time;
    }
}
