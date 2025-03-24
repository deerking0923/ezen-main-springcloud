# Dadok Dadok — MSA Version (Backend)

## 프로젝트 소개
Microservices-based reading tracker service (Proof‑of‑Concept)

## 프로젝트 상태
 Proof‑of‑Concept: Successfully registered microservices with Eureka service registry; service-to-service communication tested locally. Spring Cloud discovery & config setup; cloud deployment pending due to memory constraints.

## 주요 기능
- Service registration & discovery (Eureka)
- API gateway
- Config management (Spring Cloud Config)

## 기술 스택
- Java 17
- Spring Cloud (Eureka, Config, apigateway)
- Docker Compose
- MySQL

## 설치 및 실행 (로컬)
```bash
git clone https://github.com/username/dadok-msa-backend.git
cd dadok-msa-backend
docker-compose up --build
