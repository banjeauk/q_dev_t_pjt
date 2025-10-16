# 시스템 아키텍처

## 전체 아키텍처

### 1. 시스템 구성도
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Browser   │───▶│  Spring Boot    │───▶│   H2 Database   │
│   (Frontend)    │    │   Application   │    │   (File-based)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 2. 레이어 구조
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

### 1. Controller Layer
- **HomeController**: 메인 페이지 및 네비게이션
- **LoginController**: 인증 관련 처리
- **ChatController**: 채팅 기능 처리

### 2. Service Layer
- **UserService**: 사용자 관리 비즈니스 로직
- 향후 확장: ChatService, AIService

### 3. Repository Layer
- **UserRepository**: 사용자 데이터 접근
- Spring Data JPA 기반 자동 구현

### 4. Entity Layer
- **User**: 사용자 정보 엔티티
- **ChatHistory**: 채팅 기록 엔티티

## 데이터베이스 설계

### 1. ERD
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

### 2. 테이블 관계
- users (1) : (N) chat_sessions
- 현재 chat_history는 독립적으로 운영

## 기술 스택 상세

### 1. Backend Framework
- **Spring Boot 3.5.6**: 메인 프레임워크
- **Spring Data JPA**: ORM 및 데이터 접근
- **Spring Web MVC**: 웹 계층
- **Spring Security**: 향후 보안 강화 예정

### 2. Frontend
- **Thymeleaf**: 서버사이드 템플릿 엔진
- **HTML/CSS/JavaScript**: 기본 웹 기술
- **Bootstrap**: UI 프레임워크 (선택적)

### 3. Database
- **H2 Database**: 파일 기반 관계형 데이터베이스
- **Hibernate**: JPA 구현체

### 4. Build & Deployment
- **Gradle**: 빌드 도구
- **Spring Boot DevTools**: 개발 편의성

## 보안 아키텍처

### 1. 현재 구현
- 세션 기반 인증
- 기본적인 로그인/로그아웃

### 2. 향후 개선 계획
- Spring Security 적용
- JWT 토큰 기반 인증
- 비밀번호 암호화 (BCrypt)
- CSRF 보호

## 성능 고려사항

### 1. 데이터베이스
- H2 파일 기반으로 단일 서버 환경에 적합
- 대용량 처리 시 MySQL/PostgreSQL 전환 고려

### 2. 캐싱
- 향후 Redis 캐시 도입 검토
- 세션 클러스터링 고려

### 3. 확장성
- 마이크로서비스 아키텍처로 전환 가능
- API Gateway 도입 고려

## 모니터링 및 로깅

### 1. 현재 구현
- Spring Boot Actuator (기본 설정)
- 콘솔 로깅

### 2. 향후 개선
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Prometheus + Grafana
- 분산 트레이싱 (Zipkin/Jaeger)

## 배포 아키텍처

### 1. 개발 환경
```
Developer Machine
├── IDE (Eclipse/IntelliJ)
├── Local H2 Database
└── Embedded Tomcat
```

### 2. 프로덕션 환경 (권장)
```
Load Balancer
├── Application Server 1
│   ├── Spring Boot JAR
│   └── H2 Database
└── Application Server 2
    ├── Spring Boot JAR
    └── Shared Database
```

## API 설계 원칙

### 1. RESTful 설계
- 리소스 기반 URL 구조
- HTTP 메서드 적절한 사용
- 상태 코드 표준 준수

### 2. 응답 형식
- JSON 기반 API 응답
- 일관된 에러 응답 구조
- 페이지네이션 지원

## 확장 계획

### 1. 단기 계획
- AI API 연동 (OpenAI, Claude 등)
- 실시간 채팅 (WebSocket)
- 파일 업로드 기능

### 2. 장기 계획
- 마이크로서비스 분리
- 클라우드 네이티브 전환
- 다국어 지원