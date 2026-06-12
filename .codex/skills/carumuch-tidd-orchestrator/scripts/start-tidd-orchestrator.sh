#!/usr/bin/env bash
set -euo pipefail

ticket_id="${1:-}"

if [[ -z "$ticket_id" ]]; then
  echo "usage: $0 <ticket-id>" >&2
  exit 1
fi
workspace="${CMUX_WORKSPACE_ID:-$(cmux current-workspace)}"
repo_root="$(git rev-parse --show-toplevel)"
repo_name="$(basename "$repo_root")"
parent_dir="$(dirname "$repo_root")"
main_surface="${CMUX_SURFACE_ID:-}"
ticket_file="$repo_root/docs/tickets/TICKET-$ticket_id.md"
result_timeout_sec="${TIDD_RESULT_TIMEOUT_SEC:-1800}"
current_branch="$(git branch --show-current)"

require_bin() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "missing required command: $1" >&2
    exit 1
  fi
}

surface_by_title() {
  local title="$1"
  cmux tree --workspace "$workspace" \
    | awk -v title="$title" '$0 ~ "\"" title "\"" {for (i = 1; i <= NF; i++) if ($i ~ /^surface:/) {print $i; exit}}'
}

parse_field() {
  local text="$1"
  local prefix="$2"
  awk -v prefix="$prefix" '{
    for (i = 1; i <= NF; i++) {
      if ($i ~ ("^" prefix ":")) {
        print $i
        exit
      }
    }
  }' <<<"$text"
}

main_pane_from_surface() {
  local surface="$1"
  cmux tree --workspace "$workspace" | awk -v surface="$surface" '
    /pane pane:/ {
      for (i = 1; i <= NF; i++) if ($i ~ /^pane:/) pane = $i
    }
    $0 ~ surface { print pane; exit }
  '
}

close_if_present() {
  local title="$1"
  local surface
  surface="$(surface_by_title "$title" || true)"
  if [[ -n "$surface" ]]; then
    cmux close-surface --workspace "$workspace" --surface "$surface" >/dev/null
  fi
}

parse_original_branch() {
  awk -F'`' '/- 원본 브랜치:/ { print $2; exit }' "$ticket_file"
}

ensure_branch_from_develop() {
  local branch="$1"

  if ! git show-ref --verify --quiet "refs/heads/develop"; then
    echo "develop branch not found locally" >&2
    exit 1
  fi

  if ! git show-ref --verify --quiet "refs/heads/$branch"; then
    git branch "$branch" develop >/dev/null
  fi
}

wait_for_codex_ready() {
  local surface="$1"
  local attempt
  local screen

  for attempt in $(seq 1 30); do
    screen="$(cmux read-screen --workspace "$workspace" --surface "$surface" --lines 60 2>/dev/null || true)"
    if grep -Eq 'OpenAI Codex|directory:|gpt-' <<<"$screen"; then
      return 0
    fi
    sleep 1
  done

  echo "codex did not become ready on $surface" >&2
  exit 1
}

send_ticket_prompt() {
  local surface="$1"
  local agent_name="$2"
  local role_instruction="$3"
  local prompt_text

  prompt_text=$(cat <<EOF
$ticket_file 를 기준으로 작업을 시작하세요.

반드시 먼저 아래를 읽으세요.
- AGENTS.md
- docs/architecture.md
- docs/code-style.md
- docs/testing-guide.md
- $ticket_file
- .codex/skills/carumuch-dev-cycle/SKILL.md

당신의 역할은 $agent_name 입니다.
$role_instruction

작업 원칙:
- 반드시 .codex/skills/carumuch-dev-cycle/SKILL.md 를 따라 개발하세요.
- implementer -> verifier -> reviewer 순서를 지키세요.
- verifier 단계에서는 bash scripts/verify.sh 만 실행하세요.
- 티켓 범위를 벗어나지 마세요.
- 변경 전 관련 코드를 먼저 읽으세요.
- 필요한 테스트를 함께 수정하세요.
- 최종 검증은 bash scripts/verify.sh 기준으로 설명 가능해야 합니다.
- 작업 완료 시 마지막 메시지에 아래 두 줄을 정확히 출력하세요.
- TIDD_SUMMARY: 한 줄 요약
- TIDD_RECOMMENDATION: 왜 이 안을 선택할지 한 줄 추천

먼저 티켓과 관련 코드 구조를 파악한 뒤, 짧은 작업 계획을 세우고 구현을 진행하세요.
EOF
)

  cmux send --workspace "$workspace" --surface "$surface" "$prompt_text" >/dev/null
  cmux send-key --workspace "$workspace" --surface "$surface" Enter >/dev/null
}

extract_result_field() {
  local screen="$1"
  local label="$2"
  printf '%s\n' "$screen" \
    | sed -n "s/^.*$label:[[:space:]]*//p" \
    | tail -n 1
}

collect_agent_results() {
  local start_ts
  local now_ts
  local elapsed
  local screen

  start_ts="$(date +%s)"

  while :; do
    if [[ -z "${agent_a_summary:-}" || -z "${agent_a_recommendation:-}" ]]; then
      screen="$(cmux read-screen --workspace "$workspace" --surface "$agent_a_surface" --scrollback --lines 4000 2>/dev/null || true)"
      [[ -n "${agent_a_summary:-}" ]] || agent_a_summary="$(extract_result_field "$screen" "TIDD_SUMMARY")"
      [[ -n "${agent_a_recommendation:-}" ]] || agent_a_recommendation="$(extract_result_field "$screen" "TIDD_RECOMMENDATION")"
    fi

    if [[ -z "${agent_b_summary:-}" || -z "${agent_b_recommendation:-}" ]]; then
      screen="$(cmux read-screen --workspace "$workspace" --surface "$agent_b_surface" --scrollback --lines 4000 2>/dev/null || true)"
      [[ -n "${agent_b_summary:-}" ]] || agent_b_summary="$(extract_result_field "$screen" "TIDD_SUMMARY")"
      [[ -n "${agent_b_recommendation:-}" ]] || agent_b_recommendation="$(extract_result_field "$screen" "TIDD_RECOMMENDATION")"
    fi

    if [[ -n "${agent_a_summary:-}" && -n "${agent_a_recommendation:-}" && -n "${agent_b_summary:-}" && -n "${agent_b_recommendation:-}" ]]; then
      return 0
    fi

    now_ts="$(date +%s)"
    elapsed=$((now_ts - start_ts))
    if (( elapsed >= result_timeout_sec )); then
      echo "timed out while waiting for agent summaries (${result_timeout_sec}s)" >&2
      return 1
    fi

    sleep 5
  done
}

print_final_report() {
  printf '\n== Agent Results ==\n'
  printf '\n[agent-a]\n'
  printf '작업 요약: %s\n' "${agent_a_summary:-미수집}"
  printf '추천안: %s\n' "${agent_a_recommendation:-미수집}"
  printf '\n[agent-b]\n'
  printf '작업 요약: %s\n' "${agent_b_summary:-미수집}"
  printf '추천안: %s\n' "${agent_b_recommendation:-미수집}"
}

ensure_worktree() {
  local branch="$1"
  local path="$2"
  local base_ref="$3"

  if git worktree list --porcelain | awk -v path="$path" '
      $1 == "worktree" { current = $2 }
      $1 == "branch" && current == path { found = 1 }
      END { exit(found ? 0 : 1) }
    '; then
    return 0
  fi

  if [[ -e "$path" ]]; then
    echo "path exists but is not a registered worktree: $path" >&2
    exit 1
  fi

  if git show-ref --verify --quiet "refs/heads/$branch"; then
    git worktree add "$path" "$branch" >/dev/null
  else
    git worktree add -b "$branch" "$path" "$base_ref" >/dev/null
  fi
}

require_bin cmux
require_bin git
require_bin codex

if [[ ! -f "$ticket_file" ]]; then
  echo "ticket file not found: $ticket_file" >&2
  exit 1
fi

ticket_branch="$(parse_original_branch)"
if [[ -z "$ticket_branch" ]]; then
  echo "original branch not found in ticket: $ticket_file" >&2
  exit 1
fi

ensure_branch_from_develop "$ticket_branch"

ticket_branch_path="$parent_dir/${repo_name}-ticket-${ticket_id//\//-}"
ensure_worktree "$ticket_branch" "$ticket_branch_path" "$ticket_branch"

agent_a_branch="agent-a/$ticket_id"
agent_b_branch="agent-b/$ticket_id"
agent_a_path="$parent_dir/${repo_name}-agent-a-${ticket_id//\//-}"
agent_b_path="$parent_dir/${repo_name}-agent-b-${ticket_id//\//-}"

ensure_worktree "$agent_a_branch" "$agent_a_path" "$ticket_branch"
ensure_worktree "$agent_b_branch" "$agent_b_path" "$ticket_branch"

main_pane=""
if [[ -n "$main_surface" ]]; then
  main_pane="$(main_pane_from_surface "$main_surface" || true)"
fi

close_if_present "agent-b"
close_if_present "agent-a"

agent_a_result="$(cmux new-pane --direction right --workspace "$workspace")"
agent_a_surface="$(parse_field "$agent_a_result" surface)"
agent_a_pane="$(parse_field "$agent_a_result" pane)"

if [[ -z "$agent_a_surface" || -z "$agent_a_pane" ]]; then
  echo "failed to create agent-a pane" >&2
  exit 1
fi

cmux rename-tab --workspace "$workspace" --surface "$agent_a_surface" agent-a >/dev/null
cmux send --workspace "$workspace" --surface "$agent_a_surface" "cd \"$agent_a_path\" && codex" >/dev/null
cmux send-key --workspace "$workspace" --surface "$agent_a_surface" Enter >/dev/null

cmux focus-pane --workspace "$workspace" --pane "$agent_a_pane" >/dev/null

agent_b_result="$(cmux new-pane --direction down --workspace "$workspace")"
agent_b_surface="$(parse_field "$agent_b_result" surface)"
agent_b_pane="$(parse_field "$agent_b_result" pane)"

if [[ -z "$agent_b_surface" || -z "$agent_b_pane" ]]; then
  echo "failed to create agent-b pane" >&2
  exit 1
fi

cmux rename-tab --workspace "$workspace" --surface "$agent_b_surface" agent-b >/dev/null
cmux send --workspace "$workspace" --surface "$agent_b_surface" "cd \"$agent_b_path\" && codex" >/dev/null
cmux send-key --workspace "$workspace" --surface "$agent_b_surface" Enter >/dev/null

wait_for_codex_ready "$agent_a_surface"
wait_for_codex_ready "$agent_b_surface"

send_ticket_prompt \
  "$agent_a_surface" \
  "agent-a" \
  "최소한의 범위만 수정해서 티켓 요구사항을 충족하세요. 불필요한 리팩터링이나 구조 변경은 하지 마세요."

send_ticket_prompt \
  "$agent_b_surface" \
  "agent-b" \
  "같은 티켓 요구사항을 충족하되, 유지보수성을 향상시키는 방향으로 개선하세요. 네이밍, 계층 책임, 테스트 구조 개선은 허용되지만 티켓 범위를 넘는 기능 추가는 하지 마세요."

if ! collect_agent_results; then
  if [[ -n "$main_pane" ]]; then
    cmux focus-pane --workspace "$workspace" --pane "$main_pane" >/dev/null
  fi

  cmux notify \
    --workspace "$workspace" \
    --title "tidd-orchestrator failed" \
    --body "failed to collect agent results within ${result_timeout_sec}s" >/dev/null || true

  echo "failed to collect all agent results within timeout (${result_timeout_sec}s)" >&2
  exit 1
fi

if [[ -n "$main_pane" ]]; then
  cmux focus-pane --workspace "$workspace" --pane "$main_pane" >/dev/null
fi

print_final_report

cmux notify \
  --workspace "$workspace" \
  --title "tidd-orchestrator ready" \
  --body "current=$current_branch ticket=$ticket_branch(worktree) agent-a=$agent_a_branch(worktree) agent-b=$agent_b_branch(worktree)" >/dev/null || true

echo "workspace=$workspace"
echo "current branch=$current_branch"
echo "ticket branch=$ticket_branch path=$ticket_branch_path"
echo "agent-a branch=$agent_a_branch path=$agent_a_path surface=$agent_a_surface pane=$agent_a_pane"
echo "agent-b branch=$agent_b_branch path=$agent_b_path surface=$agent_b_surface pane=$agent_b_pane"
