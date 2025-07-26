# 🎵 JPOP 블로그 프로젝트

> 일본 음악 팬들을 위한 커뮤니티 블로그 서비스  
> Spring Boot 기반 백엔드 + Thymeleaf 프론트엔드 구현

---

## 📌 목차
- [1. 프로젝트 소개](#1-프로젝트-소개)
- [2. 주요 기능](#2-주요-기능)
- [3. 기술 스택](#3-기술-스택)
- [4. 실행 화면](#4-실행-화면)

---

## 1. 프로젝트 소개

JPOP 음악 팬들을 위한 커뮤니티 서비스로,  
회원들은 자유롭게 글을 작성하고 댓글을 달며 소통할 수 있습니다.

- 도메인 중심 설계 (Post, User, Comment, Like, Report 등)
- Enum 기반 태그 분류 (NEW, REVIEW, ARTIST, TALK)
- OAuth2 소셜 로그인 및 예외 처리 포함
- 실시간 좋아요/신고 기능 + 관리자 대시보드 확장 예정

---

## 2. 주요 기능

- 회원가입 및 로그인 (OAuth2)
-  게시글 작성/조회/수정/삭제
-  댓글 작성/삭제
-  좋아요 기능
-  사용자 신고 기능
-  태그 기반 카테고리 분류
-  인기글 정렬, 검색 기능

---

## 3. 기술 스택

| 분류 | 기술                                |
|------|-----------------------------------|
| Language | Java 17                           |
| Framework | Spring Boot, Spring Security, JPA |
| Database | MySQL, ~~Redis~~                      |
| Build Tool | Gradle                            |
| Frontend | Thymeleaf, html/css               |
| Deployment | ~~AWS EC2, RDS, Nginx~~           |
| Version Control | ~~Git, GitHub~~                   |
| CI/CD | ~~GitHub Actions~~                |
###### 취소선으로 표시된 기술 스택은 현재 적용되지는 않았지만, 추후 도입을 계획하고 있는 기술입니다.

## 4. 실행 화면
<details>
    <summary>홈화면</summary>
    ![홈화면 스크린샷](./images/게시판 목록.png)
</details>