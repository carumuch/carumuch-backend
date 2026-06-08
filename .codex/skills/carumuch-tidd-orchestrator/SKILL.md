---
name: carumuch-tidd-orchestrator
description: Use when the user wants a TiDD-style pair of Codex helper panes in the current cmux workspace, each isolated in its own git worktree and branch
---

# TiDD Orchestrator

## Overview

이 스킬은 현재 `cmux` 워크스페이스에서 TiDD 작업용 보조 pane 2개를 표준 레이아웃으로 띄우는 절차입니다.
현재 사용자가 작업 중인 브랜치와 worktree는 건드리지 않는 것이 전제입니다.

- 우측 상단: `agent-a`
- 우측 하단: `agent-b`

각 pane은 같은 작업 디렉터리를 공유하지 않고, 서로 다른 git worktree에서 `codex`를 실행합니다.
브랜치와 worktree는 아래 형식으로 분리합니다.

- 현재 메인 pane: 사용자의 현재 브랜치 유지
- 별도 핵심 티켓 브랜치 worktree
- `agent-a/<ticket-id>`
- `agent-b/<ticket-id>`

예:

- 현재 메인 pane: `chore/147-agent-parallel-tidd-harness` 유지
- 핵심 티켓 브랜치 worktree: `chore/144-improve-bodyshop-keyword-search`
- `agent-a/144`
- `agent-b/144`

같은 ticket id를 두 agent 모두 공유하되, 시작 전략은 다르게 둡니다.

- `agent-a`: 티켓 범위를 벗어나지 않는 최소 변경 구현
- `agent-b`: 같은 티켓 범위 안에서 유지보수성까지 고려한 구현

두 agent 모두 실제 개발 절차는 반드시 [carumuch-dev-cycle](/Users/yeongmujo/Desktop/Repository/carumuch-backend/.codex/skills/dev-cycle/SKILL.md)를 따릅니다.

## When to Use

- 사용자가 현재 `cmux` 워크스페이스에 TiDD용 보조 agent 2개를 띄우고 싶을 때
- 각 agent를 독립 브랜치와 worktree로 분리해서 병렬 작업시키고 싶을 때
- 같은 `TICKET-<id>.md`를 기준으로 서로 다른 구현 전략을 비교하고 싶을 때
- 좌측 메인 pane은 유지한 채 우측에 표준 보조 레이아웃을 만들고 싶을 때

사용하지 않을 때:

- 단일 agent만 필요한 경우
- 기존 우측 pane 레이아웃을 그대로 보존해야 하는 경우
- `cmux` 없이 일반 셸만 사용하는 경우

## Layout Contract

- 현재 포커스된 메인 pane은 좌측에 유지합니다.
- 현재 메인 pane의 브랜치와 작업 디렉터리는 변경하지 않습니다.
- `agent-a`를 먼저 오른쪽에 생성합니다.
- `agent-a` pane을 기준으로 아래에 `agent-b`를 생성합니다.
- 마지막에는 메인 pane으로 포커스를 돌립니다.

## Git Isolation Contract

- 현재 사용자가 작업 중인 브랜치와 현재 worktree는 변경하지 않습니다.
- `docs/tickets/TICKET-<ticket-id>.md`에 적힌 `원본 브랜치`를 핵심 티켓 브랜치로 사용합니다.
- 핵심 티켓 브랜치가 없으면 `develop`을 기준으로 생성합니다.
- 핵심 티켓 브랜치는 현재 worktree에 checkout 하지 않고, 별도 worktree로 준비합니다.
- `agent-a`와 `agent-b`는 핵심 티켓 브랜치에서 직접 작업하지 않습니다.
- `agent-a`는 `agent-a/<ticket-id>` 브랜치가 연결된 별도 worktree에서 작업합니다.
- `agent-b`는 `agent-b/<ticket-id>` 브랜치가 연결된 별도 worktree에서 작업합니다.
- `agent-a/<ticket-id>`, `agent-b/<ticket-id>`는 모두 핵심 티켓 브랜치 기준 커밋으로 생성합니다.
- 기본 worktree 경로는 메인 저장소의 상위 디렉터리에 아래 형식으로 생성합니다.
  - `<repo-name>-ticket-<ticket-id>`
  - `<repo-name>-agent-a-<ticket-id>`
  - `<repo-name>-agent-b-<ticket-id>`
- 기존 worktree가 이미 있으면 재사용합니다.
- 기존 worktree에 미커밋 변경이 있어도 자동 삭제하거나 초기화하지 않습니다.

## Workflow

1. 현재 pane이 메인 좌측 pane으로 남아야 하는지 확인합니다.
2. `cmux`, `git`, `codex` 명령이 있는지 확인합니다.
3. `docs/tickets/TICKET-<ticket-id>.md`가 존재하는지 확인합니다.
4. 티켓에서 `원본 브랜치`를 읽습니다.
5. 핵심 티켓 브랜치를 현재 worktree와 분리된 별도 worktree로 준비합니다.
6. `agent-a/<ticket-id>`, `agent-b/<ticket-id>`용 개별 worktree를 핵심 티켓 브랜치 기준으로 준비합니다.
7. 기존 `agent-a`, `agent-b` pane이 남아 있으면 닫고 표준 레이아웃으로 다시 만듭니다.
8. `agent-a`, `agent-b` pane을 생성하고 각각 해당 worktree로 이동한 뒤 `codex`를 실행합니다.
9. 두 agent 모두 같은 `TICKET-<ticket-id>.md`와 `carumuch-dev-cycle`을 읽도록 시작 프롬프트를 보냅니다.
10. 역할은 아래처럼 분리합니다.
   - `agent-a`: 최소 변경, 티켓 범위 엄수, 불필요한 리팩터링 금지
   - `agent-b`: 같은 티켓 범위 안에서 유지보수성 개선 허용
11. 두 agent가 작업을 마치면 각자 고정 형식으로 아래 두 줄을 출력하게 합니다.
   - `TIDD_SUMMARY: ...`
   - `TIDD_RECOMMENDATION: ...`
12. 메인 pane 스크립트가 위 두 줄을 회수해 최종 요약을 출력합니다.
13. `cmux tree`와 각 pane의 화면을 읽어 결과를 확인합니다.

## Command

저장소 루트 기준 실행 예시는 아래와 같습니다.

```bash
bash .codex/skills/tidd-orchestrator/scripts/start-tidd-orchestrator.sh 147
bash .codex/skills/tidd-orchestrator/scripts/start-tidd-orchestrator.sh 203
```

인자는 필수이며, 넘긴 ticket id를 그대로 브랜치 suffix로 사용합니다.
동시에 `docs/tickets/TICKET-<ticket-id>.md`를 기준 티켓으로 사용합니다.

예를 들어 `144`를 넘기고 티켓의 `원본 브랜치`가 `chore/144-improve-bodyshop-keyword-search`라면 아래처럼 맞춥니다.

- 현재 메인 pane: 현재 사용자의 브랜치 그대로 유지
- 별도 핵심 티켓 브랜치 worktree: `chore/144-improve-bodyshop-keyword-search`
- `agent-a/144`
- `agent-b/144`

이때 `chore/144-improve-bodyshop-keyword-search`가 없으면 `develop` 기준으로 생성합니다.
그리고 두 agent는 각자 별도 worktree에서 작업하지만, 기준점은 모두 이 핵심 티켓 브랜치입니다.

## Agent Strategy

### agent-a

- `TICKET-<id>.md`의 작업 범위를 벗어나지 않습니다.
- 반드시 `carumuch-dev-cycle`을 사용해 implementer -> verifier -> reviewer 순서를 따릅니다.
- 최소한의 파일과 최소한의 수정만으로 티켓을 해결합니다.
- 구조 개선이나 리팩터링은 티켓 달성에 꼭 필요한 경우만 허용합니다.
- 작업 완료 시 마지막 메시지에 `TIDD_SUMMARY:`와 `TIDD_RECOMMENDATION:`을 한 줄씩 출력합니다.

### agent-b

- 같은 `TICKET-<id>.md`를 기준으로 작업합니다.
- 반드시 `carumuch-dev-cycle`을 사용해 implementer -> verifier -> reviewer 순서를 따릅니다.
- 기능 구현은 동일하게 달성하되, 유지보수성을 높이는 방향을 우선합니다.
- 네이밍, 계층 책임, 테스트 구조, repository 분리처럼 장기 유지에 도움이 되는 개선을 허용합니다.
- 단, 티켓 범위를 벗어나는 기능 추가는 금지합니다.
- 작업 완료 시 마지막 메시지에 `TIDD_SUMMARY:`와 `TIDD_RECOMMENDATION:`을 한 줄씩 출력합니다.

## Notes

- 이 스킬은 우측 보조 pane 2개만 다룹니다.
- 메인 pane의 기존 작업 디렉터리와 프로세스는 건드리지 않습니다.
- 메인 pane의 현재 브랜치를 다른 티켓 브랜치로 checkout 하면 안 됩니다.
- 부분적으로만 남은 stale pane이 있으면 정리 후 다시 생성합니다.
- worktree 생성에 실패하면 pane 생성 전에 중단하는 것이 원칙입니다.
- 티켓 파일이 없으면 pane 생성 전에 중단해야 합니다.
- 메인 pane은 두 agent의 `TIDD_SUMMARY`와 `TIDD_RECOMMENDATION`을 출력한 뒤 종료해야 합니다.
