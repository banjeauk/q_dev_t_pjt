# GitHub 업로드 가이드

## 1. Git 설치 (필요한 경우)
- https://git-scm.com/download/win 에서 Git for Windows 다운로드 및 설치

## 2. GitHub 저장소 생성
1. GitHub.com에 로그인
2. "New repository" 클릭
3. Repository name: `q_dev_t_pjt`
4. Description: `Spring Boot AI 동화 생성 웹 애플리케이션`
5. Public 또는 Private 선택
6. "Create repository" 클릭

## 3. 로컬에서 Git 초기화 및 업로드
```bash
# 프로젝트 디렉토리로 이동
cd c:\Users\minos\git\q_dev_t_pjt\q_dev_t_pjt

# Git 초기화
git init

# 모든 파일 추가
git add .

# 첫 번째 커밋
git commit -m "Initial commit: AI 동화 생성 웹 애플리케이션"

# GitHub 저장소와 연결 (YOUR_USERNAME을 실제 GitHub 사용자명으로 변경)
git remote add origin https://github.com/YOUR_USERNAME/q_dev_t_pjt.git

# main 브랜치로 푸시
git branch -M main
git push -u origin main
```

## 4. 환경변수 설정 (배포 시)
```bash
# AWS 토큰 환경변수 설정
set AWS_BEDROCK_TOKEN=your-actual-token-here
```

## 5. 주요 기능
- ✅ Spring Boot 3.5.6 기반 웹 애플리케이션
- ✅ AI 동화 생성 (AWS Bedrock 연동)
- ✅ 이미지 생성 (AWS Titan Image Generator)
- ✅ 사용자 인증 시스템
- ✅ H2 데이터베이스 연동
- ✅ 책장 넘기기 스타일 동화 뷰어
- ✅ 반응형 웹 디자인

## 6. 보안 고려사항
- AWS 자격 증명은 환경변수로 관리
- .gitignore로 민감한 파일 제외
- 프로덕션 환경에서는 IAM 역할 사용 권장

## 7. 실행 방법
```bash
./gradlew bootRun
```
- 접속: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console