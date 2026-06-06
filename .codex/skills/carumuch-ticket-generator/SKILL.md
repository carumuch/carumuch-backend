---
name: carumuch-ticket-generator
description: Use when the user provides a GitHub issue number in this repository and wants a TiDD ticket created from `docs/tickets/TEMPLATE.md`
---

# Ticket Generator

## Overview

이 스킬은 이 저장소에서 GitHub 이슈 번호를 받아 `docs/tickets/TEMPLATE.md` 형식의 TiDD 티켓을 만드는 작업 절차입니다.

목표는 이슈 내용을 그대로 복사하는 것이 아니라, 현재 코드 구조와 템플릿 규칙에 맞게 실행 가능한 티켓으로 구체화하는 것입니다.

## When to Use

- 사용자가 특정 이슈 번호로 티켓 생성을 요청할 때
- 결과물이 `docs/tickets/TEMPLATE.md` 형식을 따라야 할 때
- 이슈 설명이 짧아서 코드 탐색으로 범위를 보강해야 할 때

사용하지 않을 때:

- 이미 티켓 초안이 있고 단순 수정만 요청받은 경우
- 구현이나 코드 변경이 목적이고 티켓 작성이 목적이 아닌 경우

## Workflow

1. 저장소 규칙을 먼저 읽습니다.
   - `AGENTS.md`
   - `docs/architecture.md`
   - `docs/code-style.md`
   - `docs/testing-guide.md`
   - `docs/tickets/TEMPLATE.md`
2. 현재 저장소의 원격 저장소를 확인합니다.
   - 가능하면 GitHub connector로 해당 저장소의 이슈 본문과 코멘트를 함께 읽습니다.
3. 이슈만으로 범위가 부족하면 관련 코드와 테스트를 탐색합니다.
   - `rg`로 관련 controller, service, repository, test를 찾습니다.
   - 현재 구현이 무엇을 지원하는지 확인한 뒤 티켓 범위를 구체화합니다.
4. 티켓 파일을 `docs/tickets/TICKET-<issue-number>.md`로 생성합니다.
5. 템플릿의 모든 항목을 채웁니다.
   - `작업 범위`는 실제 수정 계층과 의도를 함께 씁니다.
   - `작업 제외 범위`는 이번 티켓에서 하지 않을 일을 분명히 씁니다.
   - 정보가 부족하면 추측으로 채우지 말고 `비고`에 가정이나 확인 필요 사항을 남깁니다.
6. 문서 작성 후 `./scripts/verify.sh`를 실행합니다.

## Writing Rules

- 티켓 ID는 `TICKET-<issue-number>` 형식을 사용합니다.
- 원본 이슈는 `#<issue-number>` 형식으로 적습니다.
- 원본 브랜치는 이슈 제목의 타입을 우선 반영해 `type/<issue-number>-summary` 형태로 제안합니다.
- 제목은 한 줄로 작업 목적이 드러나야 합니다.
- 예상 변경 파일은 실제 현재 구조를 기준으로 작성합니다.
- 완료 조건과 검증 방법은 실행 가능해야 합니다.
- 권장 커밋 메시지는 `type: 작업 요약 #<issue-number>` 형식을 사용합니다.

## Practical Heuristics

- 이슈 제목에 `[Chore]`, `[Refactor]`, `[Feat]`, `[Fix]` 같은 타입이 있으면 브랜치와 커밋 메시지에 반영합니다.
- 이슈 본문에 검색 조건, 도메인 속성, API 범위가 모호하면 관련 패키지를 읽고 현재 구현 기준으로 최소 범위를 정의합니다.
- 범위를 넓히는 문구보다 독립 커밋 가능한 최소 작업 단위로 정리합니다.
- 구현 세부사항이 미확정이면 `구현 메모`와 `비고`에 제약과 확인 포인트를 분리해 적습니다.

## Common Mistakes

- 템플릿 문구를 그대로 두고 실제 내용으로 치환하지 않는 것
- 이슈 본문을 요약만 하고 현재 코드 구조를 반영하지 않는 것
- 작업 범위와 작업 제외 범위를 섞어 쓰는 것
- 검증 단계를 생략하는 것
- 근거 없이 파일 경로나 구현 방식을 과하게 단정하는 것
