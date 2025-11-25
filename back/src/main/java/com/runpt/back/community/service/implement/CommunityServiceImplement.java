package com.runpt.back.community.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.runpt.back.community.dto.request.*;
import com.runpt.back.community.dto.response.*;
import com.runpt.back.community.entity.CommentEntity;
import com.runpt.back.community.entity.CommunityEntity;
import com.runpt.back.community.repository.CommentRepository;
import com.runpt.back.community.repository.CommunityRepository;
import com.runpt.back.community.service.CommunityService;
import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.repository.TierRepository;
import com.runpt.back.user.repository.UserRepository;
import com.runpt.back.user.entity.TierEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImplement implements CommunityService{

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;

    @Override
    public ResponseEntity<? super PostResponseDto> post(PostRequestDto dto) {
        try {
            Long uid = dto.getUid();

            String nickname = userRepository.findById(uid)
                .map(user -> user.getNickname())
                .orElse("알 수 없음");

            TierEntity tierEntity = tierRepository.findByUid(uid);
            String tier = (tierEntity != null) ? tierEntity.getShortTierRank() : "UNRANKED";

            LocalDateTime t = LocalDateTime.now();
            CommunityEntity entity = new CommunityEntity(dto, t, nickname, tier);
            communityRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostResponseDto.success();
    }


    @Override
    public ResponseEntity<? super HomeResponseDto> getList(HomeRequestDto dto) {
        List<CommunityEntity> entityList = null;
        try {
            // 1. uid 로 프로필 찾기
            //UserEntity user = userRepository.findByUid(request.getUid());
            //if (user == null) {
            //throw new RuntimeException("프로필을 찾을 수 없습니다.");
            //}

            // 2. 성별 가져오기
            String userGender = "MALE"; //user.getGender();   // "MALE", "FEMALE"

            // 3. 필터링 조건 만들기
            List<String> genderFilter;

            if ("MALE".equalsIgnoreCase(userGender)) {
                genderFilter = List.of("MALE", "ALL");
            } else if ("FEMALE".equalsIgnoreCase(userGender)) {
                genderFilter = List.of("FEMALE", "ALL");
            } else {
                genderFilter = List.of("ALL");
            }

            // 4. 게시글 조회, 최근 만들어진 게시물 부터 나오게
            entityList = communityRepository.findByTargetgenderInOrderByCreateAtDesc(genderFilter);
        } catch (Exception e) {
            
        }
        return HomeResponseDto.success(entityList);
    }

    @Override
    public ResponseEntity<? super DetailResponseDto> getDetail(DetailRequestDto dto){
        CommunityEntity community = null;
        List<CommunityCommentResponseDto> comments = null;
        try {
             Long id = dto.getId();

            community = communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

            List<CommentEntity> commentList = 
                commentRepository.findByCommunityidOrderByCreateAtAsc(id);

            comments = commentList.stream()
                .map(comment -> {
                    Long uid = comment.getUid();
                    
                    String nickname = userRepository.findById(uid)
                        .map(user -> user.getNickname())
                        .orElse("알 수 없음");

                    TierEntity tierentity = tierRepository.findByUid(uid);
                    String tier = (tierentity != null) ? tierentity.getShortTierRank() : "UNRANKED"; 

                    return new CommunityCommentResponseDto(comment, nickname, tier);
                })
                .toList();

            

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DetailResponseDto.success(community, comments);
    }

    @Override
    public ResponseEntity<? super CommentResponseDto> writeComment(CommentRequestDto dto) {
        try {
            LocalDateTime t = LocalDateTime.now();
            CommentEntity comment = new CommentEntity(dto, t);

            commentRepository.save(comment); 

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CommentResponseDto.success();
    }
}
    
