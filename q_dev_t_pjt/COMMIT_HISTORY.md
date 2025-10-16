# 개발 히스토리

## 주요 개발 단계

### 1. 프로젝트 초기 설정
- Spring Boot 3.5.6 프로젝트 생성
- H2 데이터베이스 설정
- 기본 MVC 구조 구성

### 2. 사용자 인증 시스템
- User 엔티티 및 Repository 구현
- 로그인/회원가입 기능
- 세션 관리

### 3. AI 동화 생성 기능
- AWS Bedrock 연동
- Story, StoryPage 엔티티 설계
- AI 텍스트 생성 서비스 구현

### 4. 이미지 생성 기능
- AWS Titan Image Generator 연동
- 동화 내용 기반 이미지 프롬프트 생성
- 한국어 → 영어 번역 로직

### 5. 동화 뷰어 개선
- 책장 넘기기 스타일 UI 구현
- 2페이지 스프레드 뷰
- 반응형 디자인

### 6. 성능 및 UX 개선
- 8페이지 한번에 생성
- 로딩 페이지 개선
- 콘텐츠 간결화
- 이미지-텍스트 매칭 정확도 향상

### 7. 보안 강화
- AWS 자격 증명 환경변수화
- .gitignore 설정
- 민감한 정보 제거

## 기술 스택
- **Backend**: Spring Boot 3.5.6, Spring Data JPA
- **Frontend**: Thymeleaf, HTML/CSS/JavaScript
- **Database**: H2 Database
- **AI Services**: AWS Bedrock (Titan Text, Titan Image)
- **Build Tool**: Gradle
- **Java Version**: 17

## 주요 파일 구조
```
src/
├── main/
│   ├── java/com/kyobodts/q_dev_t_pjt/
│   │   ├── controller/          # 웹 컨트롤러
│   │   ├── entity/              # JPA 엔티티
│   │   ├── repository/          # 데이터 접근 계층
│   │   ├── service/             # 비즈니스 로직
│   │   └── config/              # 설정 클래스
│   └── resources/
│       ├── templates/           # Thymeleaf 템플릿
│       │   └── story/           # 동화 관련 템플릿
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
└── test/                        # 테스트 코드
```