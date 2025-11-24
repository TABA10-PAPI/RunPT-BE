package com.runpt.back.global.helper;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.runpt.back.global.dto.KakaoUserInfo;

@Component
public class KakaoOauthHelper {

    private final RestTemplate restTemplate = new RestTemplate();

    // -----------------------------------------------------------
    //  accessToken → userInfo(id + nickname)
    // -----------------------------------------------------------
    public KakaoUserInfo getUserInfoFromToken(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            userInfoUri,
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = response.getBody();

        if (body == null) return null;

        // id 꺼내기
        String id = String.valueOf(body.get("id"));

        // kakao_account 꺼내기
        Map<String, Object> account = (Map<String, Object>) body.get("kakao_account");
        if (account == null) return new KakaoUserInfo(id, null);

        // profile 꺼내기
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        String nickname = null;
        if (profile != null && profile.get("nickname") != null) {
            nickname = profile.get("nickname").toString();
        }

        return new KakaoUserInfo(id, nickname);
    }
}
