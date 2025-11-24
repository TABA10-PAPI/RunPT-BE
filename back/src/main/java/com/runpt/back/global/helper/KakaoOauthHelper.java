package com.runpt.back.global.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.runpt.back.global.dto.KakaoUserInfo;

@Component
public class KakaoOauthHelper {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    // -----------------------------------------------------------
    //  access token 획득
    // -----------------------------------------------------------
    public String getAccessToken(String code, String redirectUri) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&code=" + code;

        HttpEntity<String> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                tokenRequest,
                Map.class
        );

        Map response = tokenResponse.getBody();
        if (response == null) return null;

        Object token = response.get("access_token");
        return token != null ? token.toString() : null;
    }

    // -----------------------------------------------------------
    //  accessToken → userInfo(id + nickname)
    // -----------------------------------------------------------
    public KakaoUserInfo getUserInfoFromToken(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = userInfoResponse.getBody();
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

    // -----------------------------------------------------------
    //  code → KakaoUserInfo(id, nickname)
    // -----------------------------------------------------------
    public KakaoUserInfo getKakaoUserInfo(String code, String redirectUri) {
        String accessToken = getAccessToken(code, redirectUri);
        if (accessToken == null) return null;

        return getUserInfoFromToken(accessToken);
    }
}
