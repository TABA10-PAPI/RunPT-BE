package com.runpt.back.community.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.runpt.back.community.dto.request.*;
import com.runpt.back.community.dto.response.*;
import com.runpt.back.community.entity.CommentEntity;
import com.runpt.back.community.entity.CommunityEntity;
import com.runpt.back.community.entity.ParticipateEntity;
import com.runpt.back.community.repository.CommentRepository;
import com.runpt.back.community.repository.CommunityRepository;
import com.runpt.back.community.repository.ParticipateRepository;
import com.runpt.back.community.service.CommunityService;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.repository.TierRepository;
import com.runpt.back.user.repository.UserRepository;
import com.runpt.back.user.repository.RunningSessionRepository;

import jakarta.transaction.Transactional;

import com.runpt.back.user.entity.TierEntity;
import com.runpt.back.user.entity.UserEntity;
import com.runpt.back.user.entity.RunningSessionEntity;
import com.runpt.back.user.util.TierCalculator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImplement implements CommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final ParticipateRepository participaterepository;
    private final RunningSessionRepository runningSessionRepository;

    @Override
    public ResponseEntity<? super PostResponseDto> post(PostRequestDto dto) {
        try {
            Long uid = dto.getUid();

            if (uid == null || uid <= 0) {
                return PostResponseDto.uidNotExist();
            }

            UserEntity user = userRepository.findById(uid).orElse(null);
            if (user == null) {
                return PostResponseDto.userNotExist();
            }
            
            int distance = runningSessionRepository.findTop1ByUser_IdOrderByDateDesc(uid)
                    .map(RunningSessionEntity::getDistance)
                    .orElse(0);
            TierEntity tierEntity = tierRepository.findByUser_Id(uid);
            String tier = TierCalculator.calculateHighestTier(tierEntity, distance);

            LocalDateTime t = LocalDateTime.now();
            CommunityEntity entity = new CommunityEntity(dto, t, user, tier);
            communityRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CommunityHomeResponseDto> getList(CommunityHomeRequestDto dto) {
        List<CommunityEntity> entityList = null;
        try {
            Long uid = dto.getUid();

            if (uid == null || uid <= 0) {
                return CommunityHomeResponseDto.uidNotExist();
            }
            // 1. uid 로 프로필 찾기
            UserEntity user = userRepository.findById(uid)
                    .orElse(null);
            if (user == null)
                return CommunityHomeResponseDto.userNotExist();

            // 2. 성별 가져오기
            String userGender = user.getGender(); // "M", "F"
            if (userGender == null || (!userGender.equals("M") && !userGender.equals("F"))) {
                return CommunityHomeResponseDto.userGenderInvalid();
            }

            if (userGender.equals("M")) {
                entityList = communityRepository
                        .findByTargetgenderOrTargetgenderOrderByCreateAtDesc("M", "ALL");
            } else if (userGender.equals("F")) {
                entityList = communityRepository
                        .findByTargetgenderOrTargetgenderOrderByCreateAtDesc("F", "ALL");
            } else {
                return CommunityHomeResponseDto.userGenderInvalid();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CommunityHomeResponseDto.success(entityList);
    }

    @Override
    public ResponseEntity<? super DetailResponseDto> getDetail(DetailRequestDto dto) {
        CommunityEntity community = null;
        List<CommunityCommentResponseDto> comments = null;
        try {
            Long id = dto.getId();

            if (id == null || id <= 0)
                return DetailResponseDto.invalidId();

            community = communityRepository.findById(id)
                    .orElse(null);
            if (community == null)
                return DetailResponseDto.communityNotFound();

            List<CommentEntity> commentList = commentRepository.findByCommunity_IdOrderByCreateAtAsc(id);

            if (commentList == null)
                return DetailResponseDto.commentNotFound();

            try {
                comments = commentList.stream()
                        .map(comment -> {
                            Long uid = comment.getUser().getId();

                            UserEntity user = comment.getUser();
                            String nickname = user.getNickname();
                            int distance = runningSessionRepository.findTop1ByUser_IdOrderByDateDesc(uid)
                                    .map(RunningSessionEntity::getDistance)
                                    .orElse(0);
                            TierEntity tierEntity = tierRepository.findByUser_Id(uid);
                            String tier = TierCalculator.calculateHighestTier(tierEntity, distance);

                            return new CommunityCommentResponseDto(comment, nickname, tier);
                        })
                        .toList();
            } catch (RuntimeException e) {
                return DetailResponseDto.userNotExist();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        String nickname = community.getUser().getNickname();
        Long uid = community.getUser().getId();
        int distance = runningSessionRepository.findTop1ByUser_IdOrderByDateDesc(uid)
                .map(RunningSessionEntity::getDistance)
                .orElse(0);
        TierEntity tierEntity = tierRepository.findByUser_Id(uid);
        String tier = TierCalculator.calculateHighestTier(tierEntity, distance);

        return DetailResponseDto.success(community, nickname, tier, comments);
    }

    @Override
    public ResponseEntity<? super CommentResponseDto> writeComment(CommentRequestDto dto) {
        try {
            Long uid = dto.getUid();
            Long communityid = dto.getCommunityid();
            
            UserEntity user = userRepository.findById(uid).orElse(null);
            if (user == null) {
                return CommentResponseDto.userNotExist();
            }
            
            CommunityEntity community = communityRepository.findById(communityid).orElse(null);
            if (community == null) {
                return CommentResponseDto.communityNotFound();
            }
            
            LocalDateTime t = LocalDateTime.now();
            CommentEntity comment = new CommentEntity(dto, t, user, community);

            community.increaseCommentCount();
            communityRepository.save(community);
            commentRepository.save(comment);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CommentResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super DeleteResponseDto> delete(DeleteRequestDto dto) {
        Long uid = dto.getUid();
        Long id = dto.getId();

        try {
            if (uid == null || uid <= 0) return DeleteResponseDto.uidNotExist();

            if (uid == null || uid <= 0) return DeleteResponseDto.uidNotExist();

            CommunityEntity entity = communityRepository.findById(id)
                    .orElse(null);

            if (entity == null) return DeleteResponseDto.communityNotFound();

            if (!entity.getUser().getId().equals(uid)) {
                return DeleteResponseDto.forbidden(); // 작성자가 아님
            }

            participaterepository.deleteAllByCommunity_Id(id);

            commentRepository.deleteAllByCommunity_Id(id);

            communityRepository.deleteByUser_IdAndId(uid, id);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteResponseDto.success();
    }

    @Override
    public ResponseEntity<? super ModifyResponseDto> modify(ModifyRequestDto dto) {
        Long uid = dto.getUid();
        Long id = dto.getId();
    
        try {
            if (uid == null || uid <= 0) return ModifyResponseDto.uidNotExist();
            if (id == null || id <= 0) return ModifyResponseDto.invalidId();

            CommunityEntity entity = communityRepository.findById(id)
                    .orElse(null);
            if (entity == null) return ModifyResponseDto.communityNotFound();

            if (!entity.getUser().getId().equals(uid)) return ModifyResponseDto.forbidden(); // 작성자가 아님
            
            entity.update(dto);
            communityRepository.save(entity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ModifyResponseDto.success();
    }

    @Override
    public ResponseEntity<? super ParticipateResponseDto> participate(ParticipateRequestDto dto) {

        Long uid = dto.getUid();
        Long communityid = dto.getCommunityid();
        try {
            if (uid == null || uid <= 0) {
                return ParticipateResponseDto.uidNotExist();
            }
            if (communityid == null || communityid <= 0) {
                return ParticipateResponseDto.invalidId();
            }

            CommunityEntity community = communityRepository.findById(communityid)
                    .orElse(null);
            if (community == null) {
                return ParticipateResponseDto.communityNotFound();
            }

            ParticipateEntity existingParticipation = participaterepository.findByUser_IdAndCommunity_Id(uid, communityid);
            if (existingParticipation != null) {
                return ParticipateResponseDto.alreadyParticipated();
            }
            community.increaseParticipant();
            communityRepository.save(community);

            UserEntity user = userRepository.findById(uid).orElse(null);
            if (user == null) {
                return ParticipateResponseDto.uidNotExist();
            }

            ParticipateEntity entity = new ParticipateEntity(dto, user, community);
            participaterepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ParticipateResponseDto.success();
    }

    @Transactional
    @Override
    public ResponseEntity<? super ParticipateCancelResponseDto> participatecancel(ParticipateCancelRequestDto dto) {

        Long uid = dto.getUid();
        Long communityid = dto.getCommunityid();
        try {

            if (uid == null || uid <= 0) {
                return ParticipateCancelResponseDto.uidNotExist();
            }
            if (communityid == null || communityid <= 0) {
                return ParticipateCancelResponseDto.invalidId();
            }

            CommunityEntity community = communityRepository.findById(communityid)
                    .orElse(null);
            if (community == null) {
                return ParticipateCancelResponseDto.communityNotFound();
            }   
            ParticipateEntity existingParticipation = participaterepository.findByUser_IdAndCommunity_Id(uid, communityid);
            if (existingParticipation == null) {
                return ParticipateCancelResponseDto.notParticipated();
            }
            community.decreaseParticipant();
            communityRepository.save(community);

            ParticipateEntity entity = participaterepository.findByUser_IdAndCommunity_Id(uid, communityid);
            participaterepository.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ParticipateCancelResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckParticipateResponseDto> checkparticipate(CheckParticipateRequestDto dto) {
        Long communityid = dto.getCommunityid();
        List<ParticipateEntity> participates = null;
        try {
            if (communityid == null || communityid <= 0) {
            return CheckParticipateResponseDto.invalidId();
            }
            participates = participaterepository.findByCommunity_Id(communityid);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CheckParticipateResponseDto.success(participates);
    }

    @Transactional
    @Override
    public ResponseEntity<? super CommentDeleteResponseDto> commentdelete(CommentDeleteRequestDto dto) {
        Long uid = dto.getUid();
        Long communityid = dto.getCommunityid();
        CommentEntity comment = null;
        try {
            if (uid == null || uid <= 0) {
                return CommentDeleteResponseDto.uidNotExist();
            }
            if (communityid == null || communityid <= 0) return CommentDeleteResponseDto.invalidId();
            comment = commentRepository.findByUser_IdAndCommunity_Id(uid, communityid);
            if (comment == null) {
                return CommentDeleteResponseDto.commenNotFound();
            }
            
            CommunityEntity community = communityRepository.findById(communityid).orElse(null);
            if (community != null) {;
                commentRepository.delete(comment);
                community.decreaseCommentCount();
                communityRepository.save(community);
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CommentDeleteResponseDto.success();
    }

}
