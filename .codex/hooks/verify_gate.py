#!/usr/bin/env python3
import json
import re
import subprocess
import sys
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[2]
BACKEND_CHANGE_PATTERN = re.compile(
    r"^(src/|build\.gradle|build\.gradle\.kts|settings\.gradle|settings\.gradle\.kts|gradle/|gradlew|gradlew\.bat)"
)
VERIFY_COMMAND_PATTERN = re.compile(r"(^|[ /])(\.?/)?scripts/verify\.sh($|[ \"'])")


def read_payload():
    try:
        return json.load(sys.stdin)
    except Exception:
        return None


def list_changed_files(diff_args):
    proc = subprocess.run(
        ["git", "diff", *diff_args, "--name-only"],
        cwd=PROJECT_ROOT,
        stdout=subprocess.PIPE,
        stderr=subprocess.DEVNULL,
        text=True,
        check=False,
    )
    return [line.strip() for line in proc.stdout.splitlines() if line.strip()]


def has_backend_related_changes():
    changed_files = set(list_changed_files(["--cached"])) | set(list_changed_files([]))
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

    latest_result = None
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

            raw_output = payload.get("aggregated_output")
            latest_result = {
                "exit_code": payload.get("exit_code"),
                "output": "" if raw_output is None else str(raw_output)[-4000:],
            }

    return latest_result


def block(reason):
    print(json.dumps({
        "decision": "block",
        "reason": reason,
    }))


def main():
    payload = read_payload()
    if payload is None:
        sys.exit(0)

    if payload.get("stop_hook_active"):
        sys.exit(0)

    if not has_backend_related_changes():
        sys.exit(0)

    verify_result = get_latest_verify_result(
        payload.get("transcript_path"),
        payload.get("turn_id"),
    )

    if verify_result is None:
        block(
            "백엔드 관련 변경이 감지되었습니다.\n\n"
            "종료하기 전에 `bash scripts/verify.sh`를 먼저 실행하고 결과를 확인하세요."
        )
        sys.exit(0)

    if verify_result.get("exit_code") != 0:
        block(
            "Verification failed.\n\n"
            "실패 원인을 수정한 뒤 `bash scripts/verify.sh`를 다시 실행하세요.\n\n"
            "Recent output:\n"
            f"{verify_result.get('output', '')}"
        )
        sys.exit(0)


if __name__ == "__main__":
    main()
