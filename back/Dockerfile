# 1. 베이스 이미지 설정: 경량화된 Java Runtime Environment (JRE) 21 사용
#    빌드 속도와 이미지 크기 최적화를 위해 JRE만 포함된 이미지를 선택합니다.
FROM eclipse-temurin:21-jre-jammy

# 2. 작업 디렉토리 설정: 컨테이너 내부의 모든 명령이 /app 디렉토리 기준으로 실행됩니다.
WORKDIR /app

# 3. JAR 파일 복사
#    - [호스트 경로]: back-0.0.1-SNAPSHOT.jar (deploy.yml에서 back/ 폴더로 복사한 파일)
#    - [컨테이너 경로]: app.jar (컨테이너 내에서 실행될 파일 이름)
#    **주의:** 이 경로는 GitHub Actions의 빌드 컨텍스트(./back)를 기준으로 합니다.
COPY back-0.0.1-SNAPSHOT.jar app.jar

# 4. 포트 노출 (문서화 목적): Spring Boot 애플리케이션이 8080 포트에서 실행됨을 외부에 알립니다.
EXPOSE 8080

# 5. 애플리케이션 실행 명령: 컨테이너가 시작될 때 실행될 명령어입니다.
#    -e 옵션으로 전달된 DB 환경 변수는 이 시점에 Spring Boot에 전달됩니다.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]