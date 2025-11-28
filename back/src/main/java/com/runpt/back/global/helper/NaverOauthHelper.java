package com.runpt.back.global.helper;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.runpt.back.global.dto.NaverUserInfo;

@Component
public class NaverOauthHelper {

    private final RestTemplate restTemplate = new RestTemplate();

    // -----------------------------------------------------------
    //  accessToken → userInfo(id + nickname)
    // -----------------------------------------------------------
    public NaverUserInfo getUserInfoFromToken(String accessToken) {
        String userInfoUri = "https://openapi.naver.com/v1/nid/me";

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

        // 네이버 API 응답 구조 확인
        String resultcode = (String) body.get("resultcode");
        if (!"00".equals(resultcode)) {
            return null;
        }

        // response 객체에서 사용자 정보 추출
        Map<String, Object> responseData = (Map<String, Object>) body.get("response");
        if (responseData == null) return null;

        // id 꺼내기
        String id = String.valueOf(responseData.get("id"));

        // nickname 꺼내기
        String nickname = null;
        if (responseData.get("nickname") != null) {
            nickname = responseData.get("nickname").toString();
        }

        return new NaverUserInfo(id, nickname);
    }
}
