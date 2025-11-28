// package com.runpt.back.global.helper;

// import java.util.Map;

// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.*;
// import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestTemplate;

// import com.runpt.back.global.dto.NaverUserInfo;

// @Component
// public class NaverOauthHelper {
//     private final RestTemplate restTemplate = new RestTemplate();

//     public NaverUserInfo getUserInfo(String accessToken) {
//         String userInfoUri = "https://openapi.naver.com/v1/nid/me";

//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "Bearer " + accessToken);

//         HttpEntity<?> request = new HttpEntity<>(headers);

//         ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
//             userInfoUri,
//             HttpMethod.GET,
//             request,
//             new ParameterizedTypeReference<Map<String, Object>>() {}
//         );

//         Map<String, Object> body = response.getBody();

//         if (body == null || !"success".equals(body.get("resultcode"))) {
//             return null;
//         }

//         Map<String, Object> responseData = (Map<String, Object>) body.get("response");

//         String id = String.valueOf(responseData.get("id"));
//         String nickname = (String) responseData.get("nickname");

//         return new NaverUserInfo(id, nickname);
//     }
// }
