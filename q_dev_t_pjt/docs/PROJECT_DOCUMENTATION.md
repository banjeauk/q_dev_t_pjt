# Q Dev T Project - 프로젝트 문서

---

# 1. 프로젝트 개요

## 프로젝트 소개
Spring Boot 기반의 AI 채팅 웹 애플리케이션입니다. 사용자 인증과 채팅 기록 관리 기능을 제공합니다.

## 기술 스택
- **Backend**: Spring Boot 3.5.6, Spring Data JPA
- **Frontend**: Thymeleaf, HTML/CSS/JavaScript
- **Database**: H2 Database (파일 기반)
- **Build Tool**: Gradle
- **Java Version**: 17

## 주요 기능
- 사용자 회원가입/로그인
- AI 채팅 인터페이스
- 채팅 기록 저장 및 조회
- H2 데이터베이스 콘솔 접근

## 프로젝트 구조
```
src/
├── main/
│   ├── java/com/kyobodts/q_dev_t_pjt/
│   │   ├── controller/          # 웹 컨트롤러
│   │   ├── entity/              # JPA 엔티티
│   │   ├── repository/          # 데이터 접근 계층
│   │   ├── service/             # 비즈니스 로직
│   │   └── QDevTPjtApplication.java
│   └── resources/
│       ├── templates/           # Thymeleaf 템플릿
│       ├── application.properties
│       ├── schema.sql           # DB 스키마
│       └── data.sql             # 초기 데이터
└── test/                        # 테스트 코드
```

---

# 2. 시스템 아키텍처

## 전체 아키텍처
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Browser   │───▶│  Spring Boot    │───▶│   H2 Database   │
│   (Frontend)    │    │   Application   │    │   (File-based)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 레이어 구조
```
┌─────────────────────────────────────────┐
│              Presentation Layer          │
│         (Controllers, Templates)        │
├─────────────────────────────────────────┤
│               Service Layer             │
│            (Business Logic)             │
├─────────────────────────────────────────┤
│             Repository Layer            │
│            (Data Access)                │
├─────────────────────────────────────────┤
│               Entity Layer              │
│            (Domain Models)              │
└─────────────────────────────────────────┘
```

## 컴포넌트 상세

### Controller Layer
- **HomeController**: 메인 페이지 및 네비게이션
- **LoginController**: 인증 관련 처리
- **ChatController**: 채팅 기능 처리

### Service Layer
- **UserService**: 사용자 관리 비즈니스 로직

### Repository Layer
- **UserRepository**: 사용자 데이터 접근

### Entity Layer
- **User**: 사용자 정보 엔티티
- **ChatHistory**: 채팅 기록 엔티티

## 데이터베이스 설계

### ERD
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      users      │    │ chat_sessions   │    │  chat_history   │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ id (PK)         │    │ id (PK)         │    │ id (PK)         │
│ username        │◄──┐│ user_id (FK)    │    │ user_message    │
│ password        │   └┤ session_name    │    │ ai_response     │
│ email           │    │ created_at      │    │ created_at      │
│ created_at      │    └─────────────────┘    └─────────────────┘
└─────────────────┘
```

---

# 3. API 명세서

## 인증
세션 기반 인증을 사용합니다.

## 엔드포인트

### 메인 페이지
- **URL**: `/`
- **Method**: GET
- **Description**: 애플리케이션 메인 페이지
- **Response**: index.html 템플릿

### 로그인
#### 로그인 페이지
- **URL**: `/login`
- **Method**: GET
- **Description**: 로그인 폼 페이지
- **Response**: login.html 템플릿

#### 로그인 처리
- **URL**: `/login`
- **Method**: POST
- **Parameters**:
  - `username` (String): 사용자명
  - `password` (String): 비밀번호
- **Success Response**: 대시보드로 리다이렉트
- **Error Response**: 로그인 페이지로 리다이렉트

### 회원가입
#### 회원가입 페이지
- **URL**: `/register`
- **Method**: GET
- **Description**: 회원가입 폼 페이지
- **Response**: register.html 템플릿

#### 회원가입 처리
- **URL**: `/register`
- **Method**: POST
- **Parameters**:
  - `username` (String): 사용자명
  - `password` (String): 비밀번호
  - `email` (String): 이메일
- **Success Response**: 로그인 페이지로 리다이렉트
- **Error Response**: 회원가입 페이지로 리다이렉트

### 채팅
#### 채팅 페이지
- **URL**: `/chat`
- **Method**: GET
- **Description**: AI 채팅 인터페이스
- **Authentication**: 로그인 필요
- **Response**: chat.html 템플릿

#### 채팅 메시지 전송
- **URL**: `/chat`
- **Method**: POST
- **Parameters**:
  - `message` (String): 사용자 메시지
- **Authentication**: 로그인 필요
- **Response**: 채팅 페이지 (AI 응답 포함)

## 데이터 모델

### User
```json
{
  "id": "Long",
  "username": "String",
  "password": "String",
  "email": "String"
}
```

### ChatHistory
```json
{
  "id": "Long",
  "userMessage": "String",
  "aiResponse": "String",
  "createdAt": "LocalDateTime"
}
```

---

# 4. 설치 및 배포 가이드

## 개발 환경 설정

### 필수 요구사항
- JDK 17 이상
- Gradle 8.x
- Git

### 로컬 개발 환경 구축
```bash
# 프로젝트 클론
git clone <repository-url>
cd q_dev_t_pjt

# 의존성 설치 및 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### 접속 정보
- 메인 페이지: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console

## 데이터베이스 설정
- **JDBC URL**: `jdbc:h2:file:./data/testdb`
- **사용자명**: `sa`
- **비밀번호**: (공백)

## 프로덕션 배포

### JAR 파일 빌드
```bash
./gradlew clean build
```

### 서버 배포
```bash
# JAR 파일 실행
java -jar q_dev_t_pjt-0.0.1-SNAPSHOT.jar

# 백그라운드 실행
nohup java -jar q_dev_t_pjt-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

## Docker 배포

### Dockerfile
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY build/libs/q_dev_t_pjt-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

### Docker 실행
```bash
# 이미지 빌드
docker build -t q-dev-t-project .

# 컨테이너 실행
docker run -p 8080:8080 -v $(pwd)/data:/app/data q-dev-t-project
```

---

# 5. 보안 및 성능

## 보안 고려사항

### 현재 구현
- 세션 기반 인증
- 기본적인 로그인/로그아웃

### 향후 개선 계획
- Spring Security 적용
- JWT 토큰 기반 인증
- 비밀번호 암호화 (BCrypt)
- CSRF 보호

## 성능 고려사항

### 데이터베이스
- H2 파일 기반으로 단일 서버 환경에 적합
- 대용량 처리 시 MySQL/PostgreSQL 전환 고려

### 확장성
- 마이크로서비스 아키텍처로 전환 가능
- API Gateway 도입 고려

---

# 6. 향후 개발 계획

## 단기 계획
- AI API 연동 (OpenAI, Claude 등)
- 실시간 채팅 (WebSocket)
- 파일 업로드 기능

## 장기 계획
- 마이크로서비스 분리
- 클라우드 네이티브 전환
- 다국어 지원

---

# 7. 트러블슈팅

## 일반적인 문제
- **포트 충돌**: `server.port` 변경
- **메모리 부족**: JVM 힙 크기 조정 (`-Xmx512m`)
- **데이터베이스 락**: H2 파일 권한 확인

## 로그 분석
```bash
# 에러 로그 검색
grep "ERROR" app.log

# 특정 시간대 로그
grep "2024-01-01 10:" app.log
```

---

**문서 작성일**: 2024년
**프로젝트 버전**: 0.0.1-SNAPSHOT
**작성자**: Q Dev T Project Team