package com.runpt.back.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
// RestTemplate(HTTP 통신 도구)를 프로젝트 공용으로 하나 등록해 두는 설정 파일
// 외부 API를 한번의 설정으로 계속 호출 가능
// private final RestTemplate restTemplate; 이 코드를 위에 적어두면 사용 가능
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}