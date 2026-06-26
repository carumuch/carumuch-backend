---
name: carumuch-tidd
description: 기존 TiDD 티켓(TICKET-<번호>.md)을 기반으로 구현 작업을 시작할 때 사용
---

# carumuch-tidd

## Overview

이 스킬은 TiDD 티켓 번호를 받아 기존 티켓 문서를 기준으로 개발을 시작하게 만드는 진입 스킬입니다.

역할은 얇은 오케스트레이션에 한정합니다.
티켓 생성, 브랜치 제안, 별도 운영 절차 추가는 맡지 않습니다.

## When to Use

- 사용자가 `carumuch-tidd 144`처럼 티켓 번호를 던지고 바로 개발을 시작하길 원할 때
- 작업 기준 문서가 `docs/tickets/TICKET-<number>.md`에 있어야 할 때
- 구현은 반드시 저장소 표준 개발 사이클로 진행해야 할 때

사용하지 않을 때:

- 해당 번호의 TiDD 티켓이 아직 없을 때
- 티켓 생성 자체가 목적일 때
- 문서 작성이나 티켓 수정만 하려는 경우

## Input Contract

- 입력 예시: `carumuch-tidd 144`
- 입력 숫자는 `TICKET-144`로 정규화합니다.
- 대상 티켓 파일은 `docs/tickets/TICKET-144.md`로 해석합니다.

## Workflow

1. 저장소 규칙 문서를 먼저 읽습니다.
   - `AGENTS.md`
   - `docs/architecture.md`
   - `docs/code-style.md`
   - `docs/testing-guide.md`
2. 입력 숫자를 티켓 ID와 파일 경로로 정규화합니다.
3. `docs/tickets/TICKET-<number>.md`가 존재하는지 확인합니다.
4. 파일이 없으면 즉시 중단합니다.
   - 티켓이 없으므로 `carumuch-ticket-generator`를 먼저 사용하라고 안내합니다.
   - 티켓을 임의로 만들거나 구현을 시작하지 않습니다.
5. 파일이 있으면 해당 티켓 문서를 읽고 작업 범위, 작업 제외 범위, 완료 조건, 검증 방법을 확인합니다.
6. 티켓 기반 구현은 반드시 `carumuch-dev-cycle`로 진행합니다.

## Execution Rule

**REQUIRED SUB-SKILL:** Use carumuch-dev-cycle for all implementation work after the ticket is found.

- 구현 시작 후에는 `carumuch-dev-cycle`의 implementer, verifier, reviewer 흐름을 따릅니다.
- 최종 검증은 `carumuch-dev-cycle`이 요구하는 방식으로 수행합니다.
- 구현 범위는 `docs/architecture.md`의 작업 경계 원칙을 따르며, 명시적 요구 없이는 `src/main/java/com/carumuch/capstone/common` 하위 코드를 수정하거나 추가하지 않습니다.
- `carumuch-tidd`는 개발 진입만 담당하며 구현 절차 자체를 대체하지 않습니다.

## Hard Stops

- 티켓 파일이 없으면 진행하지 않습니다.
- `carumuch-dev-cycle`을 우회해 바로 코드를 수정하지 않습니다.
- 티켓 범위를 추측으로 보강해 개발을 시작하지 않습니다.
- 명시적 요구가 없는데 `common` 디렉터리까지 수정 범위를 넓혀 구현하지 않습니다.
