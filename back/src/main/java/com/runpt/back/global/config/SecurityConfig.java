package com.runpt.back.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())                      // CORS 비활성 (원하면 .disable() 대신 설정 가능)
            .csrf(csrf -> csrf.disable())                      // CSRF 비활성
            .httpBasic(httpBasic -> httpBasic.disable())        // 기본 로그인폼 비활성
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()                      // 모든 요청 허용
            );

        return http.build();
    }

    
}
