# 🚀 다독다독 — MSA 버전 (Backend)


## 프로젝트 소개
Spring Cloud API Gateway와 Eureka 기반의 독서 기록 서비스 마이크로서비스 백엔드 (Proof-of-Concept)

## 프로젝트 상태
Proof-of-Concept: Eureka 서비스 등록, API Gateway(`api-gateway-service`) 라우팅, 로컬 환경에서 서비스 간 통신 확인 완료, 클라우드 배포는 메모리 제약으로 보류 중

## 아키텍처 및 서비스 목록
| 서비스 | 설명 | 포트 |
|---------|-------|------|
| API Gateway (`api-gateway-service`) | 요청 라우팅 | 8000 |
| Service Discovery (`service-discovery`) | Eureka 서버 (서비스 등록·발견) | 8761 |
| User Service (`user-service`) | 사용자 CRUD | 60000 |
| Book Service (`bookreview-service`) | 도서 기록 CRUD | 40000 |
| Recent Review Service (`recentreview-service`) | 최신 리뷰 조회 | 20000 |
| MyLibrary Service (`mylibrary-service`) | 개인 도서관 관리 | 30000 |
| Community Service (`community-service`) | 커뮤니티 게시판 | 50000 |
| Config Service (`config-service`) | 중앙 설정 관리 (Spring Cloud Config) | 8888 |

## 기술 스택
- Java 17, Spring Boot, Spring Cloud (Gateway, Eureka, Config)
- MySQL
- Docker Compose

## 학습 포인트
- Spring Cloud API Gateway를 통한 요청 라우팅
- Eureka를 이용한 서비스 등록 및 발견
- 마이크로서비스 간 REST 통신 설계
- Docker Compose로 여러 서비스 오케스트레이션
- 클라우드 배포 시 메모리 제약 문제 인식 및 해결 방안 탐색

