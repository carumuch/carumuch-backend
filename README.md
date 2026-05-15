[![kaumeochi-baeneo-choesinhwa.jpg](https://i.postimg.cc/DzWzGGdP/kaumeochi-baeneo-choesinhwa.jpg)](https://postimg.cc/GBCrwHf4)

> **📢 캡스톤 디자인 경진대회 최우수상 수상작** 🏆

# 🚗 카우머치 Backend

AI 기반 차량 사고 분석 및 견적 입찰 서비스 카우머치 백엔드 서버입니다.


[🎥 **데모 영상 바로가기**](https://youtu.be/81JoqTP4Jds?si=r36hSpss8QZXQKZV)

[📄 **API 문서 서버 바로가기**](https://carumuch-api-docs.vercel.app)

<br>

# 프로젝트 개요
카우머치는 **AI 기반 자동차 사고 수리 분석 서비스**입니다. 사용자가 사고 레포트를 제출하면 **AI가 자동으로 수리 견적을 생성**해주며, 여러 공업사로부터 수리 입찰 제안을 받을 수 있습니다. 공업사는 자신들의 수리 이력과 가격 경쟁력을 바탕으로 입찰에 참여합니다.

해당 서비스는 캡스톤 디자인 경진대회에서 **최우수상**을 수상했으며, 현재는 백엔드 시스템 전반에 대한 **리팩토링을 진행 중**입니다.

<br>

## 인원 소개

|                                                                조영무                                                                |                                        정석현                                        |                                                                                                              
|:---------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------:|
| <img width="160px" src="https://avatars.githubusercontent.com/u/75081608?s=400&u=c4c22f3af10105e0fb18a9d346988e9403a533f6&v=4" alt="조영무 프로필 이미지" /> | <img width="160px" src="https://avatars.githubusercontent.com/u/113079762?v=4" alt="정석현 프로필 이미지" /> |
|                                               [@fprh13](https://github.com/fprh13)                                                |              [@jeongseockhyeon](https://github.com/jeongseockhyeon)               |
|                                        Identity, Damage,<br/> Estimate, Bodyshop, Bidding                                         |                                 Community, 이미지 처리                                 |

<br>

## 주요 도메인

- `Identity`: 회원가입, 로그인, JWT 인증, 계정 복구
- `Damage`: 차량 등록, 사고 레포트 등록/조회/수정
- `Estimate`: AI 견적 생성, 상태 변경, 조건 검색, MQ 결과 처리
- `Bodyshop`: 공업사 등록, 수정, 조회
- `Community`: 게시물 등록, 댓글 작성 (레거시 영역)
- `Bidding`: 수리 입찰 처리 (레거시 영역)

<br>

## 핵심 흐름

1. 사용자가 회원가입 및 로그인합니다.
2. 차량과 사고 레포트를 등록합니다.
3. 사고 레포트 등록 이후 AI 견적 생성 요청이 RabbitMQ로 비동기 발행됩니다.
4. AI 결과가 돌아오면 견적서가 생성됩니다.
5. 공업사는 견적을 조회하고 입찰에 참여합니다.

<br/>

## 기술 스택

- Language: Java 17
- Framework: Spring Boot 3.5.4, Spring Security, Spring Data JPA
- Query: Querydsl 5.0.0
- Database: MySQL, H2(test)
- Cache: Redis
- Messaging: RabbitMQ
- Documentation: Spring REST Docs, OpenAPI 3, Swagger UI
- Infra/Monitoring: AWS S3, CloudFront, Docker, Prometheus, Grafana, Loki

<br>

## ERD
[![carumuch-erd.png](https://i.postimg.cc/cJ1vKptQ/carumuch-erd.png)](https://postimg.cc/fVFzpgKL)

<br>

## 빠른 시작

### 요구사항

- JDK 17
- Docker / Docker Compose
- MySQL
- Redis
- RabbitMQ

<br>

### 필수 환경 변수

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

ACCESS_SECRET_KEY=
REFRESH_SECRET_KEY=
VERIFICATION_SECRET_KEY=

MAIL_ADDRESS=
MAIL_PASSWORD=
HOST_ADDRESS=

AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_S3_BUCKET=
AWS_CLOUDFRONT_DOMAIN=

RABBITMQ_USERNAME=
RABBITMQ_PASSWORD=
```

<br>

### 실행

애플리케이션 실행:

```bash
./gradlew bootRun
```

RabbitMQ와 AI Mock server 실행:

```bash
docker compose -f infrastructure/docker-compose-ai.yml up -d
```

모니터링 스택 실행:

```bash
docker compose -f infrastructure/docker-compose-monitoring.yml up -d
```

<br>

# 테스트 및 검증

검증 명령은 아래와 같습니다.

```bash
./scripts/verify.sh
```

- 기본적으로 단위 테스트를 우선 실행합니다.
- 통합 테스트 관련 변경이 있으면 `requires-infra`를 제외한 통합 테스트를 추가 실행합니다.

<br>

## 문서

- 아키텍처: [docs/architecture.md](docs/architecture.md)
- 코드 스타일: [docs/code-style.md](docs/code-style.md)
- 테스트 가이드: [docs/testing-guide.md](docs/testing-guide.md)
- 코드 리뷰 기준: [docs/code-review.md](docs/code-review.md)

<br>

## AI 협업 워크플로우

AI Agent 개발 워크플로를 함께 사용합니다.

- 저장소 규칙: `AGENTS.md`
- 기본 개발 오케스트레이터 스킬: `.codex/skills/dev-cycle/SKILL.md`
- 전담 에이전트: `implementer-agent`, `verifier-agent`, `reviewer-agent`
- 자동 훅: `.codex/hooks.json`

원칙은 `구현 -> 검증 -> 리뷰` 순서를 유지하는 것입니다.

<br>

## 패키지 구조

```text
src/main/java/com/carumuch/capstone
├── identity
├── damage
├── estimate
├── bodyshop
├── community
├── bidding
└── common
```

리팩터링된 주요 컨텍스트는 아래 계층 구조를 따릅니다.

```text
presentation
application
domain
infrastructure
```

<br>

## 현재 상태

- 리팩터링 중심 영역: `identity`, `damage`, `estimate`, `bodyshop`
- 레거시 중심 영역: `community`, `bidding`, `common.legacy`
