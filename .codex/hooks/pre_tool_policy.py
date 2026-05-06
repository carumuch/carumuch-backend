#!/usr/bin/env python3
import json
import sys


BLOCKED_PATTERNS = [
    "git push --force",
    "rm -rf /",
    "rm -rf .",
    "sudo",
]


def main():
    try:
        payload = json.load(sys.stdin)
    except Exception:
        sys.exit(0)

    tool_input = payload.get("tool_input", {})
    command = tool_input.get("command", "")

    if not command:
        sys.exit(0)

    for pattern in BLOCKED_PATTERNS:
        if pattern in command:
            print(json.dumps({
                "decision": "block",
                "reason": f"Blocked dangerous command: {pattern}"
            }))
            sys.exit(0)

    # allow는 출력하지 않고 그냥 통과
    sys.exit(0)


if __name__ == "__main__":
    main()
