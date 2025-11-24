package com.runpt.back.auth.service;

import com.runpt.back.auth.dto.LoginResponse;
import com.runpt.back.user.entity.User;
import com.runpt.back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserService userService;   
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String tokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String userInfoUri;

    public LoginResponse login(String code) {

        // 1) 카카오 Access Token 요청
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> tokenRequest =
                new HttpEntity<>(params, tokenHeaders);

        Map<String, Object> tokenResponse =
                restTemplate.postForObject(tokenUri, tokenRequest, Map.class);

        String accessToken = (String) tokenResponse.get("access_token");


        // 2) 카카오 사용자 정보 조회
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Map> userResponse = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                userRequest,
                Map.class
        );

        Map<String, Object> userInfo = userResponse.getBody();

        String kakaoUid = userInfo.get("id").toString();
        String provider = "kakao";

        // 3) 유저 조회/생성 — UserService에 위임
        UserService.UserResult result = userService.findOrCreate(provider, kakaoUid);
        User user = result.user();
        boolean isNewUser = result.isNewUser();

        // 5) 응답 반환
        return null;
    }
}