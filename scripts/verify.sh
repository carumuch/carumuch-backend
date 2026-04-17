#!/usr/bin/env bash
set -e

CHANGED_FILES="$(git diff --cached --name-only || true)"

if echo "$CHANGED_FILES" | grep -qE '^(src/|build\.gradle|build\.gradle\.kts|settings\.gradle|settings\.gradle\.kts|gradle/|gradlew|gradlew\.bat)'; then

  # 스프링 테스트 (통합 테스트 제외)
  echo "[verify] test (exclude integration)"
  ./gradlew test -DexcludeTags=integration || { echo "[fail] test"; exit 1; }

  echo "[verify] SUCCESS"
else
  echo "[skip] no backend-related changes"
fi

