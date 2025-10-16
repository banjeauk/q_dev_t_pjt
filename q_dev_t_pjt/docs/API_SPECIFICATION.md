# API 명세서

## 개요
Q Dev T Project의 REST API 및 웹 엔드포인트 명세서입니다.

## 인증
세션 기반 인증을 사용합니다.

## 엔드포인트

### 1. 메인 페이지
- **URL**: `/`
- **Method**: GET
- **Description**: 애플리케이션 메인 페이지
- **Response**: index.html 템플릿

### 2. 로그인
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
- **Error Response**: 로그인 페이지로 리다이렉트 (에러 메시지 포함)

### 3. 회원가입
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
- **Error Response**: 회원가입 페이지로 리다이렉트 (에러 메시지 포함)

### 4. 채팅
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

### 5. 대시보드
- **URL**: `/dashboard`
- **Method**: GET
- **Description**: 사용자 대시보드
- **Authentication**: 로그인 필요
- **Response**: dashboard.html 템플릿

## 에러 코드
- **401**: 인증되지 않은 사용자 (로그인 페이지로 리다이렉트)
- **404**: 페이지를 찾을 수 없음
- **500**: 서버 내부 오류

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