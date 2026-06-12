#!/usr/bin/env bash
set -euo pipefail

ticket_id="${1:-}"
selection_raw="${2:-}"
selection_normalized="$(printf '%s' "$selection_raw" | tr '[:upper:]' '[:lower:]')"

if [[ -z "$ticket_id" || -z "$selection_raw" ]]; then
  echo "usage: $0 <ticket-id> <A|B|agent-a|agent-b|cancel>" >&2
  exit 1
fi

case "$selection_normalized" in
  a|agent-a)
    selected_agent="agent-a"
    selected_branch="agent-a/$ticket_id"
    selected_title="A"
    ;;
  b|agent-b)
    selected_agent="agent-b"
    selected_branch="agent-b/$ticket_id"
    selected_title="B"
    ;;
  c|cancel|취소)
    selected_agent="cancel"
    selected_branch=""
    selected_title="CANCEL"
    ;;
  *)
    echo "invalid selection: $selection_raw" >&2
    exit 1
    ;;
esac

workspace="${CMUX_WORKSPACE_ID:-$(cmux current-workspace)}"
repo_root="$(git rev-parse --show-toplevel)"
repo_name="$(basename "$repo_root")"
parent_dir="$(dirname "$repo_root")"
main_surface="${CMUX_SURFACE_ID:-}"
ticket_file="$repo_root/docs/tickets/TICKET-$ticket_id.md"
ticket_branch_path="$parent_dir/${repo_name}-ticket-${ticket_id//\//-}"
agent_a_path="$parent_dir/${repo_name}-agent-a-${ticket_id//\//-}"
agent_b_path="$parent_dir/${repo_name}-agent-b-${ticket_id//\//-}"

require_bin() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "missing required command: $1" >&2
    exit 1
  fi
}

parse_original_branch() {
  awk -F'`' '/- 원본 브랜치:/ { print $2; exit }' "$ticket_file"
}

surface_by_title() {
  local title="$1"
  cmux tree --workspace "$workspace" \
    | awk -v title="$title" '$0 ~ "\"" title "\"" {for (i = 1; i <= NF; i++) if ($i ~ /^surface:/) {print $i; exit}}'
}

close_if_present() {
  local title="$1"
  local surface
  surface="$(surface_by_title "$title" || true)"
  if [[ -n "$surface" ]]; then
    cmux close-surface --workspace "$workspace" --surface "$surface" >/dev/null
    printf 'closed %s (%s)\n' "$title" "$surface"
  else
    printf 'not-found %s\n' "$title"
  fi
}

ensure_registered_worktree() {
  local path="$1"
  if ! git worktree list --porcelain | awk -v path="$path" '
      $1 == "worktree" && $2 == path { found = 1 }
      END { exit(found ? 0 : 1) }
    '; then
    echo "worktree not registered: $path" >&2
    exit 1
  fi
}

worktree_is_clean() {
  local path="$1"
  [[ -z "$(git -C "$path" status --short --untracked-files=normal)" ]]
}

remove_worktree_if_clean() {
  local path="$1"
  local label="$2"

  if [[ ! -d "$path" ]]; then
    printf '%s: not-found\n' "$label"
    return 0
  fi

  if ! git worktree list --porcelain | awk -v path="$path" '
      $1 == "worktree" && $2 == path { found = 1 }
      END { exit(found ? 0 : 1) }
    '; then
    printf '%s: not-registered\n' "$label"
    return 0
  fi

  if worktree_is_clean "$path"; then
    git worktree remove "$path" >/dev/null
    printf '%s: removed\n' "$label"
  else
    printf '%s: skipped-dirty\n' "$label"
  fi
}

remove_worktree_force_if_registered() {
  local path="$1"
  local label="$2"

  if [[ ! -d "$path" ]]; then
    printf '%s: not-found\n' "$label"
    return 0
  fi

  if ! git worktree list --porcelain | awk -v path="$path" '
      $1 == "worktree" && $2 == path { found = 1 }
      END { exit(found ? 0 : 1) }
    '; then
    printf '%s: not-registered\n' "$label"
    return 0
  fi

  git worktree remove --force "$path" >/dev/null
  printf '%s: force-removed\n' "$label"
}

delete_branch_if_present() {
  local branch="$1"
  local label="$2"

  if git show-ref --verify --quiet "refs/heads/$branch"; then
    git branch -D "$branch" >/dev/null
    printf '%s: deleted\n' "$label"
  else
    printf '%s: not-found\n' "$label"
  fi
}

require_bin cmux
require_bin git
require_bin bash

if [[ ! -f "$ticket_file" ]]; then
  echo "ticket file not found: $ticket_file" >&2
  exit 1
fi

ticket_branch="$(parse_original_branch)"
if [[ -z "$ticket_branch" ]]; then
  echo "original branch not found in ticket: $ticket_file" >&2
  exit 1
fi

merge_status="SKIP"
verify_status="SKIP"
verify_output="not-run"

if [[ "$selected_agent" != "cancel" ]]; then
  case "$selected_agent" in
    agent-a)
      selected_path="$agent_a_path"
      ;;
    agent-b)
      selected_path="$agent_b_path"
      ;;
  esac

  ensure_registered_worktree "$ticket_branch_path"
  ensure_registered_worktree "$selected_path"

  if ! worktree_is_clean "$ticket_branch_path"; then
    echo "ticket worktree is dirty: $ticket_branch_path" >&2
    exit 1
  fi

  if ! worktree_is_clean "$selected_path"; then
    echo "selected agent worktree is dirty: $selected_path" >&2
    exit 1
  fi

  merge_status="PASS"
  verify_status="PASS"

  if ! git -C "$ticket_branch_path" merge --no-ff "$selected_branch" -m "merge: apply $selected_branch into $ticket_branch for TICKET-$ticket_id" >/tmp/tidd-finalizer-merge.log 2>&1; then
    merge_status="FAIL"
  fi

  if [[ "$merge_status" == "PASS" ]]; then
    if ! verify_output="$(cd "$ticket_branch_path" && bash scripts/verify.sh 2>&1)"; then
      verify_status="FAIL"
    fi
  else
    verify_status="SKIP"
    verify_output="merge failed"
  fi
fi

pane_cleanup_output="$(
  {
    close_if_present "agent-a"
    close_if_present "agent-b"
  } 2>&1
)"

if [[ "$selected_agent" == "cancel" ]]; then
  worktree_cleanup_output="$(
    {
      remove_worktree_force_if_registered "$agent_a_path" "agent-a worktree"
      remove_worktree_force_if_registered "$agent_b_path" "agent-b worktree"
      remove_worktree_force_if_registered "$ticket_branch_path" "ticket worktree"
    } 2>&1
  )"

  branch_cleanup_output="$(
    {
      delete_branch_if_present "agent-a/$ticket_id" "agent-a branch"
      delete_branch_if_present "agent-b/$ticket_id" "agent-b branch"
      delete_branch_if_present "$ticket_branch" "ticket branch"
    } 2>&1
  )"
  next_step_output="current worktree는 유지되며, TiDD 자원만 정리되었습니다."
else
  ticket_worktree_cleanup_output="ticket worktree: preserved-for-inspection"

  if [[ "$merge_status" == "PASS" && "$verify_status" == "PASS" ]]; then
    ticket_worktree_cleanup_output="$(
      remove_worktree_if_clean "$ticket_branch_path" "ticket worktree" 2>&1
    )"
  fi

  if [[ "$merge_status" == "PASS" ]]; then
    worktree_cleanup_output="$(
      {
        remove_worktree_force_if_registered "$agent_a_path" "agent-a worktree"
        remove_worktree_force_if_registered "$agent_b_path" "agent-b worktree"
        printf '%s\n' "$ticket_worktree_cleanup_output"
      } 2>&1
    )"

    branch_cleanup_output="$(
      {
        delete_branch_if_present "agent-a/$ticket_id" "agent-a branch"
        delete_branch_if_present "agent-b/$ticket_id" "agent-b branch"
      } 2>&1
    )"
  else
    worktree_cleanup_output="$(
      {
        remove_worktree_if_clean "$agent_a_path" "agent-a worktree"
        remove_worktree_if_clean "$agent_b_path" "agent-b worktree"
        printf '%s\n' "$ticket_worktree_cleanup_output"
      } 2>&1
    )"
    branch_cleanup_output="branch cleanup skipped (merge failed)"
  fi

  if [[ "$ticket_worktree_cleanup_output" == "ticket worktree: removed" ]]; then
    next_step_output="main worktree에서 git checkout $ticket_branch 로 선택 결과를 확인할 수 있습니다."
  else
    next_step_output="ticket worktree가 남아 있으면 main worktree에서 git checkout $ticket_branch 가 막힐 수 있습니다. 남은 경로: $ticket_branch_path"
  fi
fi

printf '\n== TiDD Final Report ==\n'
printf '선택: %s' "$selected_title"
if [[ -n "$selected_branch" ]]; then
  printf ' (%s)\n' "$selected_branch"
else
  printf '\n'
fi
printf '핵심 티켓 브랜치: %s\n' "$ticket_branch"
printf '병합 결과: %s\n' "$merge_status"
printf '테스트 결과: %s\n' "$verify_status"
printf '\n[Pane 정리]\n%s\n' "$pane_cleanup_output"
printf '\n[Worktree 정리]\n%s\n' "$worktree_cleanup_output"
printf '\n[브랜치 정리]\n%s\n' "$branch_cleanup_output"
printf '\n[남은 작업 경로]\n'
printf 'ticket worktree: %s\n' "$ticket_branch_path"
printf 'current worktree: %s\n' "$repo_root"
printf '\n[다음 단계]\n%s\n' "$next_step_output"
printf '\n[검증 출력]\n%s\n' "$verify_output"

cmux notify \
  --workspace "$workspace" \
  --surface "$main_surface" \
  --title "tidd-finalizer complete" \
  --body "selected=${selected_branch:-cancel} merge=$merge_status verify=$verify_status" >/dev/null || true
