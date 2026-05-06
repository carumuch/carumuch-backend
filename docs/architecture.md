# Architecture

## 목적

이 문서는 `carumuch-backend`의 구조를 빠르게 이해하기 위한 아키텍처 문서입니다.

이 문서는 다음 질문에 답하기 위해 존재합니다.

- 이 프로젝트는 어떤 도메인 흐름으로 동작하는가
- 각 컨텍스트는 어떤 책임을 가지는가
- 새 코드를 어느 계층에 두어야 하는가
- 어떤 영역이 핵심 리팩터링 대상이고, 어떤 영역이 레거시인가

참고:

- `AGENTS.md`: 작업 규칙과 응답 규칙
- `docs/code-style.md`: 코드 작성 규칙
- `docs/testing-guide.md`: 검증 규칙

이 문서는 구조와 책임을 설명합니다.

실행 규칙이나 코드 스타일의 세부 기준은 각각 별도 문서를 우선합니다.

---

## 빠른 시작점

처음 진입할 때는 아래 파일부터 읽는 것이 가장 효율적입니다.

1. `src/main/java/com/carumuch/capstone/common/infrastructure/config/SecurityConfig.java`
2. `src/main/java/com/carumuch/capstone/identity/presentation/AuthController.java`
3. `src/main/java/com/carumuch/capstone/damage/application/DamageReportService.java`
4. `src/main/java/com/carumuch/capstone/estimate/infrastructure/mq/EstimateResultListener.java`
5. `src/main/java/com/carumuch/capstone/bodyshop/application/BodyShopService.java`
6. `src/main/java/com/carumuch/capstone/common/presentation/advice/ControllerExceptionAdvice.java`

위 파일들은 인증 진입점, 핵심 동기 흐름, 핵심 비동기 흐름, 공통 예외 규약을 가장 짧게 보여줍니다.

## 시스템 개요

카우머치 백엔드는 차량 사고 접수 이후의 흐름을 처리합니다.

1. 사용자가 회원가입 및 로그인합니다.
2. 사용자가 차량과 사고 레포트를 등록합니다.
3. 사고 레포트 등록 이후 AI 견적 생성 요청이 비동기로 발행됩니다.
4. 외부 AI 처리 결과가 돌아오면 견적서가 생성됩니다.
5. 공업사는 견적을 조회하고 입찰하거나 공업사 정보를 운영합니다.

현재 구조는 크게 두 축으로 나뉩니다.

- HTTP 요청을 즉시 처리하는 동기 API 흐름
- 견적 생성을 처리하는 이벤트 + RabbitMQ 기반 비동기 흐름

## 설계 원칙

### 1. 패키지는 도메인 기준으로 분리합니다

최상위 패키지는 `com.carumuch.capstone.<context>` 형태입니다.

- `identity`: 회원, 인증, 토큰, 계정 복구
- `damage`: 차량, 사고 레포트
- `estimate`: AI 견적, 검색, MQ 연동
- `bodyshop`: 공업사
- `community`, `bidding`: 기존 기능
- `common`: 공통 설정, 예외, 로깅, 웹 응답 규약

### 2. 리팩터링된 컨텍스트는 계층형 구조를 따릅니다

주요 컨텍스트는 아래 구조를 따릅니다.

```text
presentation   -> Controller, Request/Response DTO, argument resolver
application    -> 유스케이스 orchestration, 트랜잭션, 권한 검증
domain         -> Entity, Value Object, Repository interface, 정책
infrastructure -> JPA 구현체, Redis, JWT, MQ, Mail, Logging, Config
```

핵심 방향은 `domain`이 외부 기술을 직접 알지 않도록 하고, `infrastructure`가 이를 구현하는 방식입니다.

### 3. 후처리는 트랜잭션 이후 이벤트로 분리합니다

회원가입 완료, 사고 레포트 등록처럼 후속 작업이 필요한 흐름은 `@TransactionalEventListener(phase = AFTER_COMMIT)`를 사용합니다.

이 방식으로 핵심 쓰기 트랜잭션과 메일 발송, MQ 발행 같은 후속 작업을 분리합니다.

## 모듈별 구조

### identity

역할:

- 회원가입, 로그인, 토큰 재발급, 로그아웃
- 비밀번호 변경 및 계정 복구
- JWT 기반 인증

핵심 구성:

- `presentation`: `AuthController`, `UserController`, `AuthUserResolver`
- `application`: `AuthService`, `UserService`, `AccountRecoveryService`
- `domain`: `User`, `UserRepository`, `UserRegisteredEvent`, `UserWithdrawnEvent`
- `infrastructure`: `JwtTokenProvider`, `JwtAuthorizationFilter`, Redis 기반 토큰 저장소, 이벤트 핸들러

특징:

- 로그인 시에는 DB에서 사용자를 조회해 비밀번호를 검증합니다.
- 인증 필터에서는 매 요청마다 사용자 엔티티를 다시 조회하지 않고, JWT claim으로 `SecurityContext`를 구성합니다.
- 회원가입/탈퇴 후 후처리는 도메인 이벤트로 분리되어 있습니다.

### damage

역할:

- 사용자 차량 등록/수정
- 사고 레포트 등록/조회/수정

핵심 구성:

- `presentation`: `VehicleController`, `DamageReportController`
- `application`: `VehicleService`, `DamageReportService`
- `domain`: `Vehicle`, `DamageReport`, `RepairRegion`
- `infrastructure`: JPA Repository 구현체

특징:

- 사고 레포트는 `preferredRepairRegion`, `pickup 여부`, `imagePath`, `status`, `userId`를 함께 가집니다.
- 사고 레포트 등록 직후 AI 견적 생성을 위한 이벤트를 발행합니다.

### estimate

역할:

- AI 견적 저장
- 견적 상태 변경
- 조건 검색
- MQ 기반 비동기 결과 처리

핵심 구성:

- `presentation`: `EstimateController`
- `application`: `EstimateService`
- `domain`: `Estimate`, `EstimateRepository`, `EstimateStatus`
- `infrastructure.persistence`: JPA + Querydsl 검색 구현
- `infrastructure.mq`: publisher, listener, DLQ 처리

특징:

- `Estimate`는 `DamageReport`와 1:1 관계를 가집니다.
- 견적 상태 변경은 소유자 검증 후 수행합니다.
- 비정상 MQ 메시지는 DLQ로 보내고, 스케줄러가 재전송합니다.

### bodyshop

역할:

- 공업사 등록, 수정, 조회

핵심 구성:

- `presentation`: `BodyShopController`
- `application`: `BodyShopService`
- `domain`: `BodyShop`, `Location`, `PhoneNumber`
- `infrastructure`: JPA Repository 구현체

특징:

- 공업사 엔티티는 `managerUserId`를 통해 관리자를 식별합니다.
- 접근 제어는 `AccessPolicy` 구현으로 처리합니다.

### community / bidding / common.legacy

이 영역은 리팩터링된 계층 구조와 기존 구조가 공존하는 구간입니다.

특징:

- `community`, `bidding`은 `common.legacy` 예외/응답 규약을 일부 사용합니다.
- `SecurityContextHolder` 직접 조회, 레거시 이미지 서비스 의존 등 과거 방식이 남아 있습니다.
- `build.gradle`의 JaCoCo 제외 대상에도 포함되어 있어, 현재 핵심 리팩터링 영역과 분리해서 보는 것이 좋습니다.

## 요청 처리 구조

### 1. 동기 HTTP 요청

대부분의 API는 아래 경로를 따릅니다.

```text
Client
  -> Controller
  -> Application Service
  -> Domain / Repository interface
  -> Infrastructure Repository
  -> DB or external dependency
```

예시:

- 로그인: `AuthController -> AuthService -> UserRepository`
- 내 차량 조회: `VehicleController -> VehicleService -> VehicleRepository`
- 견적 상태 변경: `EstimateController -> EstimateService -> Estimate`

### 2. 예외 응답

주요 비즈니스 및 검증 예외는 `ControllerExceptionAdvice`에서 처리합니다.

- 비즈니스 예외: `CustomException`
- 요청 검증 오류: `MethodArgumentNotValidException`
- 응답 포맷: `ApiResponse`, `ApiErrorResponse`

## 비동기 견적 생성 흐름

이 프로젝트에서 가장 중요한 비동기 흐름은 사고 레포트 등록 이후 AI 견적 생성입니다.

```text
1. Client
   -> POST /damage-reports

2. DamageReportController
   -> DamageReportService.register()

3. DamageReportService
   -> DamageReport 저장
   -> DamageReportRegisteredEvent 발행

4. DamageReportRegisteredEventHandler
   -> AFTER_COMMIT 시점에 MQ 메시지 발행

5. 외부 AI 처리 시스템
   -> 손상 분석 및 견적 계산

6. RabbitMQ result queue
   -> EstimateResultListener 수신

7. EstimateResultListener
   -> DamageReport 조회
   -> Estimate 생성 및 저장

8. 실패 메시지
   -> DLQ 저장
   -> DlqRedriveScheduler 재전송
```

설계 의도:

- 사고 레포트 저장 성공과 AI 처리 요청 발행을 느슨하게 결합합니다.
- 외부 AI 처리 지연이 API 응답 시간을 직접 늘리지 않도록 합니다.
- 잘못된 메시지나 일시적 실패를 DLQ로 흡수해 재처리 가능하게 합니다.

## 인증 및 보안 구조

### 인증 방식

- Spring Security + JWT 기반 stateless 인증
- `SecurityConfig`에서 엔드포인트별 공개/인증 필요 여부를 정의
- `JwtAuthorizationFilter`가 `Authorization` 헤더의 Bearer 토큰을 파싱

### 현재 인증 흐름

```text
Client request
  -> JwtAuthorizationFilter
  -> JwtTokenProvider.parseAccessToken()
  -> claim 기반 Authentication 생성
  -> SecurityContext 저장
  -> Controller
```

특징:

- 인증 필터에서 DB 조회를 생략하여 요청당 부하를 줄입니다.
- 권한은 claim의 authority 값을 `SimpleGrantedAuthority`로 매핑합니다.
- 리프레시 토큰은 Redis 저장소를 사용합니다.

## 데이터 및 외부 의존성

### 데이터 저장소

- MySQL: 운영 영속 데이터
- H2: 테스트 프로파일
- Redis: 리프레시 토큰, 인증 코드 저장

### 메시징 / 비동기

- RabbitMQ: 견적 요청/결과 전달
- DLQ + scheduler: 실패 메시지 재처리

### 파일 / 외부 서비스

- AWS S3: 이미지 저장
- SMTP + Thymeleaf 템플릿: 메일 발송

### 운영 관측성

- MDC 기반 `traceId` 필터
- 요청/실행시간 로깅 필터 및 AOP
- Actuator + Prometheus endpoint 노출

## 공통 인프라

### 공통 설정

- `SecurityConfig`: 인증/인가, CORS
- `RedisConfig`: RedisTemplate
- `QueryDslConfig`: Querydsl 지원
- `S3Config`: S3 클라이언트 설정
- `AsyncConfig`: 이벤트 비동기 실행용 thread pool

### 로깅

- `MdcTraceIdFilter`: 요청별 traceId 생성
- `RequestLoggingFilter`: 요청 메타데이터 로깅
- `ExecutionTimeAspect`: 비즈니스 로직 실행시간 측정

`AsyncConfig`는 MDC context를 비동기 스레드로 복사하므로, 이벤트 후처리에서도 trace 추적이 가능합니다.

## 테스트 구조

테스트는 다음 계층으로 구성됩니다.

- Controller 테스트
- Application 서비스 테스트
- Domain 테스트
- Integration 테스트
- RestDocs 지원 테스트

테스트 프로파일 특징:

- DB는 H2 in-memory 사용
- Rabbit listener 자동 시작 비활성화
- 외부 의존성은 테스트용 설정값 사용

API 문서 생성은 테스트 기반으로 연결되어 있습니다.

- `test` 실행 후 `openapi3` 및 Swagger 정적 파일 복사 수행
- RestDocs + OpenAPI 3 조합으로 문서 산출

테스트 실행 방법과 종료 조건은 `docs/testing-guide.md`를 기준으로 봅니다.

## 현재 구조에서 주의할 점

### 1. 리팩터링된 영역과 레거시 영역이 공존합니다

`identity`, `damage`, `estimate`, `bodyshop`는 비교적 명확한 계층 구조를 따르지만, `community`, `bidding`, `common.legacy`는 이전 스타일이 남아 있습니다.

새 기능을 추가할 때는 가능하면 레거시 패턴을 확장하지 말고, 리팩터링된 계층 구조를 따르는 것이 좋습니다.

### 2. 이벤트 발행 방식이 혼재합니다

- `User`는 aggregate 이벤트 등록 방식 사용
- `DamageReportService`는 `ApplicationEventPublisher`를 직접 사용

둘 다 동작하지만, 장기적으로는 이벤트 발행 책임을 더 일관되게 맞추는 것이 유지보수에 유리합니다.

### 3. 일부 도메인에는 후속 개선 포인트가 남아 있습니다

예를 들어 `BodyShop.acceptCount()`에는 동시성 이슈 우려가 코드 주석으로 남아 있습니다. 이런 카운터 성격의 값은 추후 원자적 업데이트 전략 검토가 필요합니다.

## 문서를 읽는 순서

처음 파악할 때는 아래 순서를 권장합니다.

1. `src/main/java/com/carumuch/capstone/common/infrastructure/config/SecurityConfig.java`
2. `src/main/java/com/carumuch/capstone/identity`
3. `src/main/java/com/carumuch/capstone/damage`
4. `src/main/java/com/carumuch/capstone/estimate`
5. `src/main/java/com/carumuch/capstone/bodyshop`
6. `src/main/java/com/carumuch/capstone/community`
7. `src/main/java/com/carumuch/capstone/bidding`
8. `src/main/java/com/carumuch/capstone/common/legacy`

이 순서로 보면 현재의 핵심 리팩터링 구조와 과거 호환 구조를 함께 이해하기 쉽습니다.

---

## 이 문서에 없는 것

다음 내용은 이 문서의 직접 범위가 아닙니다.

- 코딩 스타일 세부 규칙
- 테스트 실행 절차와 검증 루프
- API 스펙 상세
- 운영 배포 절차

필요한 경우 아래 문서를 우선 참고합니다.

- 코드 스타일: `docs/code-style.md`
- 테스트 규칙: `docs/testing-guide.md`
