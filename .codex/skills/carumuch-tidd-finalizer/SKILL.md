---
name: carumuch-tidd-finalizer
description: Use when a user has chosen agent A, agent B, or cancel after a TiDD orchestrator run and wants to finalize the ticket resources safely
---

# TiDD Finalizer

## Overview

이 스킬은 `carumuch-tidd-orchestrator` 실행 이후 사용자가 `A`, `B`, 또는 `취소`를 선택했을 때 마무리 작업을 수행하는 절차입니다.

수행 순서는 아래와 같습니다.

1. 선택한 agent 브랜치를 핵심 티켓 브랜치에 병합
2. 핵심 티켓 브랜치 worktree에서 검증 실행
3. `agent-a`, `agent-b` pane 정리
4. agent worktree 정리
5. 최종 보고 출력

`취소`를 선택하면 병합과 테스트는 수행하지 않고, TiDD가 만든 브랜치/worktree/pane 자원을 전부 정리합니다.

현재 사용자의 메인 pane 브랜치와 worktree는 변경하지 않습니다.

## When to Use

- `carumuch-tidd-orchestrator`로 `agent-a`, `agent-b` 비교 작업을 마친 뒤 하나를 채택할 때
- 선택한 결과만 핵심 티켓 브랜치에 반영하고 싶을 때
- merge 이후 테스트, pane 정리, worktree 정리를 한 번에 마무리하고 싶을 때
- TiDD 작업 자체를 폐기하고 관련 자원을 모두 정리하고 싶을 때

사용하지 않을 때:

- 아직 어느 agent를 채택할지 결정하지 않은 경우
- 선택한 agent worktree에 미커밋 변경이 남아 있는 경우
- 핵심 티켓 브랜치가 아니라 현재 사용자 브랜치까지 바로 합치고 싶은 경우

## Merge Contract

- 병합 대상은 `선택한 agent 브랜치 -> 핵심 티켓 브랜치`까지만입니다.
- 현재 사용자의 메인 브랜치에는 자동 병합하지 않습니다.
- 핵심 티켓 브랜치는 `docs/tickets/TICKET-<ticket-id>.md`의 `원본 브랜치`를 사용합니다.
- 핵심 티켓 브랜치가 없으면 이 스킬은 새로 생성하지 않고 실패 처리합니다.
- 선택한 agent worktree 또는 핵심 티켓 worktree가 dirty하면 자동 병합하지 않습니다.
- `취소`를 선택하면 병합과 테스트를 수행하지 않습니다.

## Cleanup Contract

- `agent-a`, `agent-b` pane은 닫습니다.
- `agent-a/<ticket-id>`, `agent-b/<ticket-id>` worktree는 정리 대상으로 봅니다.
- 기본 finalize에서는 핵심 티켓 브랜치 worktree를 남깁니다.
- 현재 사용자의 메인 pane과 현재 worktree는 유지합니다.
- dirty 상태인 worktree는 강제로 삭제하지 않고, final report에 남깁니다.
- `취소`를 선택하면 아래 자원을 모두 정리 대상으로 봅니다.
  - 핵심 티켓 브랜치 worktree
  - `agent-a/<ticket-id>` worktree
  - `agent-b/<ticket-id>` worktree
  - 핵심 티켓 브랜치
  - `agent-a/<ticket-id>` 브랜치
  - `agent-b/<ticket-id>` 브랜치
- `취소` 경로는 사용자가 명시적으로 요청한 정리 작업이므로, 위 TiDD 자원에 한해 강제 정리를 허용합니다.

## Workflow

1. `docs/tickets/TICKET-<ticket-id>.md`를 읽고 `원본 브랜치`를 확인합니다.
2. 사용자가 선택한 값 `A`, `B`, `취소`를 해석합니다.
3. `A` 또는 `B`면 핵심 티켓 브랜치 worktree와 선택한 agent worktree가 모두 존재하는지 확인합니다.
4. `A` 또는 `B`면 선택한 agent worktree와 핵심 티켓 worktree가 clean 상태인지 확인합니다.
5. `A` 또는 `B`면 핵심 티켓 브랜치 worktree에서 선택한 agent 브랜치를 병합합니다.
6. `A` 또는 `B`면 핵심 티켓 브랜치 worktree에서 `bash scripts/verify.sh`를 실행합니다.
7. `agent-a`, `agent-b` pane을 닫습니다.
8. `A` 또는 `B`면 agent worktree를 정리합니다.
9. `취소`면 티켓 worktree, agent worktree, 관련 브랜치를 모두 정리합니다.
10. 병합 결과, 검증 결과, pane 정리 결과, worktree/브랜치 정리 결과를 최종 보고로 출력합니다.

## Command

저장소 루트 기준 실행 예시는 아래와 같습니다.

```bash
bash .codex/skills/tidd-finalizer/scripts/finalize-tidd.sh 144 A
bash .codex/skills/tidd-finalizer/scripts/finalize-tidd.sh 144 B
bash .codex/skills/tidd-finalizer/scripts/finalize-tidd.sh 144 cancel
```

첫 번째 인자는 ticket id, 두 번째 인자는 선택 결과입니다.

- `A`, `a`, `agent-a` 모두 `agent-a`로 처리합니다.
- `B`, `b`, `agent-b` 모두 `agent-b`로 처리합니다.
- `cancel`, `취소`, `c`는 취소 정리 경로로 처리합니다.

## Notes

- 이 스킬은 현재 사용자 브랜치를 checkout 하지 않습니다.
- 병합은 핵심 티켓 브랜치 worktree 내부에서만 수행합니다.
- 테스트 실패 시에도 pane/worktree 정리 결과는 final report에 함께 남깁니다.
- verify 실패를 숨기지 않습니다.
- `취소` 경로는 TiDD가 만든 브랜치/worktree/pane 자원을 제거하지만, 현재 사용자의 작업 브랜치와 현재 worktree는 건드리지 않습니다.
