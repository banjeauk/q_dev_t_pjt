# 배포 가이드

## 개발 환경 설정

### 1. 필수 요구사항
- JDK 17 이상
- Gradle 8.x
- Git

### 2. 로컬 개발 환경 구축
```bash
# 프로젝트 클론
git clone <repository-url>
cd q_dev_t_pjt

# 의존성 설치 및 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### 3. 개발 도구 설정
- IDE: Eclipse, IntelliJ IDEA, VS Code
- Lombok 플러그인 설치 필요
- Spring Boot DevTools로 핫 리로드 지원

## 프로덕션 배포

### 1. JAR 파일 빌드
```bash
./gradlew clean build
```
생성된 JAR 파일: `build/libs/q_dev_t_pjt-0.0.1-SNAPSHOT.jar`

### 2. 서버 배포
```bash
# JAR 파일 실행
java -jar q_dev_t_pjt-0.0.1-SNAPSHOT.jar

# 백그라운드 실행
nohup java -jar q_dev_t_pjt-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### 3. 환경별 설정

#### application-prod.properties
```properties
# 프로덕션 데이터베이스 설정
spring.datasource.url=jdbc:h2:file:./prod-data/testdb
spring.h2.console.enabled=false
spring.jpa.show-sql=false
server.port=8080
```

#### 실행 시 프로파일 지정
```bash
java -jar -Dspring.profiles.active=prod q_dev_t_pjt-0.0.1-SNAPSHOT.jar
```

## Docker 배포

### 1. Dockerfile 생성
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY build/libs/q_dev_t_pjt-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

### 2. Docker 이미지 빌드 및 실행
```bash
# 이미지 빌드
docker build -t q-dev-t-project .

# 컨테이너 실행
docker run -p 8080:8080 -v $(pwd)/data:/app/data q-dev-t-project
```

## 데이터베이스 관리

### 1. 백업
```bash
# H2 데이터베이스 파일 백업
cp -r data/ backup/data-$(date +%Y%m%d)/
```

### 2. 복원
```bash
# 백업에서 복원
cp -r backup/data-20240101/ data/
```

## 모니터링

### 1. 애플리케이션 상태 확인
- Health Check: `http://localhost:8080/actuator/health`
- H2 Console: `http://localhost:8080/h2-console` (개발 환경만)

### 2. 로그 관리
```bash
# 로그 확인
tail -f app.log

# 로그 로테이션 설정 (logback-spring.xml)
```

## 보안 고려사항

### 1. 프로덕션 환경
- H2 콘솔 비활성화
- HTTPS 설정
- 데이터베이스 암호화
- 세션 보안 강화

### 2. 방화벽 설정
- 포트 8080 개방
- 불필요한 포트 차단

## 트러블슈팅

### 1. 일반적인 문제
- **포트 충돌**: `server.port` 변경
- **메모리 부족**: JVM 힙 크기 조정 (`-Xmx512m`)
- **데이터베이스 락**: H2 파일 권한 확인

### 2. 로그 분석
```bash
# 에러 로그 검색
grep "ERROR" app.log

# 특정 시간대 로그
grep "2024-01-01 10:" app.log
```