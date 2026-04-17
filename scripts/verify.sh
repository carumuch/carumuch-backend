#!/usr/bin/env bash
set -e

CHANGED_FILES="$(git diff --cached --name-only || true)"

# 부분 스테이징 감지
PARTIAL=$(comm -12 \
    <(git diff --cached --name-only | sort -u) \
    <(git diff --name-only | sort -u))

if echo "$CHANGED_FILES" | grep -qE '^(src/|build\.gradle|build\.gradle\.kts|settings\.gradle|settings\.gradle\.kts|gradle/|gradlew|gradlew\.bat)'; then

  # 부분 스테이징 경고
  if [ -n "$PARTIAL" ]; then
      echo "[warn] 부분 스테이징 감지 (테스트는 워킹 트리 기준으로 실행됨)"
      echo "$PARTIAL" | sed 's/^/  - /'
    fi

  # 스프링 테스트 (통합 테스트 제외)
  echo "[verify] test (exclude integration)"
  ./gradlew test -DexcludeTags=integration || { echo "[fail] test"; exit 1; }

  echo "[verify] SUCCESS"
else
  echo "[skip] no backend-related changes"
fi

