# Testing Guide

## 목적

이 문서는 이 저장소의 검증 실행 규칙을 정의한다.

모든 검증은 자동화 가능한 방식으로 수행되어야 한다.

---

## 단일 진입점

검증은 반드시 아래 명령으로 수행한다:

```bash
./scripts/verify.sh
```

다른 방식의 검증은 사용하지 않는다.

`./scripts/verify.sh`의 기본 동작은 단위 테스트 우선이다.

* 기본: `@Tag("integration")`, `@Tag("requires-infra")` 테스트를 제외하고 빠르게 실행한다
* 조건부 추가 실행: 스테이징된 변경에 통합 테스트 관련 파일이 포함되면 `integration` 테스트를 추가 실행하되, `requires-infra`는 계속 제외한다

통합 테스트 관련 파일은 아래를 기준으로 판단한다:

* `src/test/java/**/integration/**`
* `IntegrationSupportTest`를 직접 수정한 경우
* `AsyncTestConfig`를 수정한 경우
* 스테이징된 테스트 클래스가 `IntegrationSupportTest`를 상속하거나 `@Tag("integration")`를 선언한 경우

`requires-infra`는 Redis, MQ처럼 별도 실행 인프라가 필요한 테스트를 위한 태그다.

---

## 실행 규칙

You MUST follow these rules:

1. 코드 변경 후 반드시 `./scripts/verify.sh`를 실행한다
2. 검증을 생략하지 않는다
3. 검증 실패 상태에서 작업을 종료하지 않는다

---

## 검증 루프

다음 과정을 반복한다:

1. 코드 변경
2. `./scripts/verify.sh` 실행
3. 결과 확인

### 실패한 경우

* 실패 원인을 분석한다
* 원인에 해당하는 코드만 수정한다
* 즉시 다시 검증을 실행한다

### 성공한 경우

* 검증을 종료한다

---

## 종료 조건

다음 조건을 모두 만족해야 작업을 종료할 수 있다:

* `./scripts/verify.sh`가 exit code 0 반환
* 기본 단위 테스트가 통과 상태
* 통합 테스트 관련 변경이 있으면 `requires-infra`를 제외한 통합 테스트까지 통과 상태
* FAIL 메시지가 없음

---

## 실패 처리 원칙

You MUST:

* 에러 메시지를 먼저 확인한다
* 실제 원인을 수정한다
* 최소 변경으로 해결한다

---

## 금지 사항

You MUST NOT:

* 테스트를 비활성화한다
* assertion을 약화한다
* 설정을 제거하여 우회한다
* mock / hardcode로 임시 통과시킨다
* 원인과 무관한 변경을 수행한다

---

## 기대 상태

작업 종료 시 반드시 다음 상태여야 한다:

* 모든 검증이 통과된 상태
* 실패를 숨기지 않은 상태
* 변경이 재현 가능한 상태
* 동일 명령으로 동일 결과가 보장되는 상태
