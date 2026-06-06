---
name: carumuch-dev-cycle
description: Use when executing backend code changes in this repository and you need a repeatable implementer-verifier-reviewer workflow with bounded retry attempts
---

# Dev Cycle

## Overview

이 문서는 이 저장소에서 백엔드 작업을 수행할 때 쓰는 기본 개발 사이클입니다.
복잡한 상태 머신 없이 역할 분리와 검증 순서를 명확히 유지하는 데 목적이 있습니다.

## When to Use

- Spring Boot 백엔드 코드를 수정할 때
- `bash scripts/verify.sh`를 최종 게이트로 유지해야 할 때
- implementer, verifier, reviewer 역할을 분리하고 싶을 때
- retry 규칙을 간단한 작업 프로토콜로 관리하고 싶을 때

사용하지 않을 때:
- 문서만 수정하는 경우
- 백엔드와 무관한 변경만 있는 경우

## Roles

- `implementer-agent`
  - 코드와 필요한 테스트를 수정합니다.
- `verifier-agent`
  - 코드를 수정하지 않고 `bash scripts/verify.sh`만 실행합니다.
- `reviewer-agent`
  - verify 성공 이후에만 리뷰합니다.

## Workflow

1. 관련 코드와 문서를 읽습니다.
2. `implementer-agent`가 변경을 수행합니다.
3. `verifier-agent`가 `bash scripts/verify.sh`를 실행합니다.
4. 실패하면 `implementer-agent`에게 실패 원인만 다시 수정시킵니다.
5. 3-4를 최대 3회 반복합니다.
6. verify 성공 시 `reviewer-agent`로 넘깁니다.
7. reviewer 결과를 정리하고 종료합니다.

## Rules

- 최종 검증은 항상 `bash scripts/verify.sh`입니다.
- verifier는 코드를 수정하지 않습니다.
- reviewer는 verify 성공 전에는 실행하지 않습니다.
- verify 실패 시 별도 fixer를 두지 않습니다.
- 3회 안에 verify가 성공하지 않으면 수동 판단이 필요하다고 보고합니다.

## Reporting Format

각 루프마다 아래를 짧게 남깁니다.

- 시도 횟수
- implementer 변경 요약
- verify 결과
- 다음 단계

## Common Mistakes

- verifier가 개별 테스트만 실행하고 종료하는 것
- verify 실패 후 무관한 리팩터링을 하는 것
- reviewer를 verify 전에 호출하는 것
- retry 규칙을 hook 상태 머신으로 다시 옮기는 것
