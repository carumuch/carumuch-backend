#!/usr/bin/env python3
import json
import re
import subprocess
import sys
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[2]
STATE_PATH = PROJECT_ROOT / "state" / "retry_state.json"
MAX_RETRIES = 3
BACKEND_CHANGE_PATTERN = re.compile(
    r"^(src/|build\.gradle|build\.gradle\.kts|settings\.gradle|settings\.gradle\.kts|gradle/|gradlew|gradlew\.bat)"
)
VERIFY_COMMAND_PATTERN = re.compile(r"(^|[ /])(\.?/)?scripts/verify\.sh($|[ \"'])")


def load_state():
    if not STATE_PATH.exists():
        return {"count": 0, "last_counted_turn_id": None}

    try:
        state = json.loads(STATE_PATH.read_text())
        return {
            "count": int(state.get("count", 0)),
            "last_counted_turn_id": state.get("last_counted_turn_id"),
        }
    except Exception:
        return {"count": 0, "last_counted_turn_id": None}


def save_state(state):
    STATE_PATH.parent.mkdir(parents=True, exist_ok=True)
    STATE_PATH.write_text(json.dumps(state, indent=2))


def reset_state():
    save_state({"count": 0, "last_counted_turn_id": None})


def get_staged_files():
    proc = subprocess.run(
        ["git", "diff", "--cached", "--name-only"],
        cwd=PROJECT_ROOT,
        stdout=subprocess.PIPE,
        stderr=subprocess.DEVNULL,
        text=True,
        check=False,
    )
    return [line.strip() for line in proc.stdout.splitlines() if line.strip()]


def get_unstaged_files():
    proc = subprocess.run(
        ["git", "diff", "--name-only"],
        cwd=PROJECT_ROOT,
        stdout=subprocess.PIPE,
        stderr=subprocess.DEVNULL,
        text=True,
        check=False,
    )
    return [line.strip() for line in proc.stdout.splitlines() if line.strip()]


def has_backend_related_changes():
    changed_files = set(get_staged_files()) | set(get_unstaged_files())
    return any(BACKEND_CHANGE_PATTERN.match(path) for path in changed_files)


def is_verify_command(command):
    if isinstance(command, list):
        command_text = " ".join(str(part) for part in command)
    else:
        command_text = str(command or "")

    return bool(VERIFY_COMMAND_PATTERN.search(command_text))


def get_latest_verify_result(transcript_path, turn_id):
    if not transcript_path:
        return None

    path = Path(transcript_path)
    if not path.exists():
        return None

    latest = None
    with path.open() as transcript:
        for line in transcript:
            try:
                event = json.loads(line)
            except Exception:
                continue

            if event.get("type") != "event_msg":
                continue

            payload = event.get("payload", {})
            if payload.get("type") != "exec_command_end":
                continue

            if payload.get("turn_id") != turn_id:
                continue

            if not is_verify_command(payload.get("command")):
                continue

            latest = {
                "exit_code": payload.get("exit_code"),
                "output": payload.get("aggregated_output", "")[-4000:],
            }

    return latest


def block(reason):
    print(json.dumps({
        "decision": "block",
        "reason": reason,
    }))


def main():
    try:
        payload = json.load(sys.stdin)
    except Exception:
        sys.exit(0)

    if payload.get("stop_hook_active"):
        sys.exit(0)

    if not has_backend_related_changes():
        reset_state()
        sys.exit(0)

    turn_id = payload.get("turn_id")
    transcript_path = payload.get("transcript_path")
    verify_result = get_latest_verify_result(transcript_path, turn_id)

    if verify_result is None:
        block(
            "백엔드 관련 변경이 감지되었습니다.\n\n"
            "종료하기 전에 `bash scripts/verify.sh`를 먼저 실행하고 결과를 확인하세요."
        )
        sys.exit(0)

    if verify_result.get("exit_code") == 0:
        reset_state()
        sys.exit(0)

    state = load_state()
    count = state.get("count", 0)
    if state.get("last_counted_turn_id") != turn_id:
        count += 1
        state["count"] = count
        state["last_counted_turn_id"] = turn_id
        save_state(state)

    if count >= MAX_RETRIES:
        block(
            "Verification failed after maximum retries.\n\n"
            f"Retry count: {count}/{MAX_RETRIES}\n\n"
            "Fix manually."
        )
        sys.exit(0)

    block(
        "Verification failed.\n\n"
        f"Retry count: {count}/{MAX_RETRIES}\n\n"
        "Fix the root cause and run again.\n\n"
        "Recent output:\n"
        f"{verify_result.get('output', '')}"
    )


if __name__ == "__main__":
    main()
