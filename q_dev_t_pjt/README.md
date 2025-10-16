# Q Dev T Project

## 프로젝트 개요
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

## 설치 및 실행

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd q_dev_t_pjt
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. 접속
- 메인 페이지: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console

## 데이터베이스 설정
- **JDBC URL**: `jdbc:h2:file:./data/testdb`
- **사용자명**: `sa`
- **비밀번호**: (공백)

## API 엔드포인트
- `GET /` - 메인 페이지
- `GET /login` - 로그인 페이지
- `POST /login` - 로그인 처리
- `GET /register` - 회원가입 페이지
- `POST /register` - 회원가입 처리
- `GET /chat` - 채팅 페이지
- `POST /chat` - 채팅 메시지 처리

## 개발 환경
- IDE: Eclipse/IntelliJ IDEA
- JDK: 17+
- Gradle: 8.14.3

## 라이센스
이 프로젝트는 교육 목적으로 개발되었습니다.