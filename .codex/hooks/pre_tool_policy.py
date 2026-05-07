#!/usr/bin/env python3
import json
import shlex
import sys


FORCE_PUSH_FLAGS = {"--force", "--force-with-lease", "-f"}
BLOCKED_COMMAND_CHECKS = [
    ("git push with force", lambda tokens: is_force_push(tokens)),
    ("rm with recursive force on / or .", lambda tokens: is_dangerous_rm(tokens)),
    ("sudo", lambda tokens: is_sudo_command(tokens)),
]


def parse_command(command):
    if isinstance(command, list):
        return [str(part) for part in command]

    try:
        return shlex.split(str(command or ""))
    except ValueError:
        return str(command or "").split()


def is_force_push(tokens):
    if len(tokens) < 3 or tokens[0] != "git" or tokens[1] != "push":
        return False

    return any(token in FORCE_PUSH_FLAGS or token.startswith("--force=") for token in tokens[2:])


def is_dangerous_rm(tokens):
    if not tokens or tokens[0] != "rm":
        return False

    flags = [token for token in tokens[1:] if token.startswith("-")]
    targets = [token for token in tokens[1:] if not token.startswith("-")]
    has_recursive_force = any("r" in flag and "f" in flag for flag in flags)
    return has_recursive_force and any(target in {"/", "."} for target in targets)


def is_sudo_command(tokens):
    return bool(tokens) and tokens[0] == "sudo"


def main():
    try:
        payload = json.load(sys.stdin)
    except Exception:
        sys.exit(0)

    tool_input = payload.get("tool_input", {})
    command = tool_input.get("command", "")

    if not command:
        sys.exit(0)

    tokens = parse_command(command)

    for reason, predicate in BLOCKED_COMMAND_CHECKS:
        if predicate(tokens):
            print(json.dumps({
                "hookSpecificOutput": {
                    "hookEventName": "PreToolUse",
                    "permissionDecision": "deny",
                    "permissionDecisionReason": f"Blocked dangerous command: {reason}",
                }
            }))
            sys.exit(0)

    # allow는 출력하지 않고 그냥 통과
    sys.exit(0)


if __name__ == "__main__":
    main()
