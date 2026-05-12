#!/usr/bin/env bash
set -euo pipefail

is_backend_related_change() {
  local file="$1"
  [[ "$file" =~ ^(src/|build\.gradle|build\.gradle\.kts|settings\.gradle|settings\.gradle\.kts|gradle/|gradlew|gradlew\.bat) ]]
}

has_staged_file() {
  local file="$1"
  git cat-file -e ":$file" 2>/dev/null
}

is_integration_test_change() {
  local file="$1"

  if [[ "$file" == "src/test/java/com/carumuch/capstone/support/IntegrationSupportTest.java" ]] || \
     [[ "$file" == "src/test/java/com/carumuch/capstone/support/config/AsyncTestConfig.java" ]]; then
    return 0
  fi

  if [[ ! "$file" =~ ^src/test/java/.*\.java$ ]]; then
    return 1
  fi

  if [[ "$file" =~ /integration/ ]]; then
    return 0
  fi

  if ! has_staged_file "$file"; then
    return 1
  fi

  git show ":$file" | grep -qE 'extends[[:space:]]+IntegrationSupportTest|@Tag\("integration"\)'
}

CHANGED_FILES="$(git diff --cached --name-only || true)"

# 부분 스테이징 감지
PARTIAL=$(comm -12 \
    <(git diff --cached --name-only | sort -u) \
    <(git diff --name-only | sort -u))

RUN_INTEGRATION_TESTS=false
RUN_BACKEND_VERIFICATION=false

while IFS= read -r changed_file; do
  [ -z "$changed_file" ] && continue

  if is_backend_related_change "$changed_file"; then
    RUN_BACKEND_VERIFICATION=true
  fi

  if is_integration_test_change "$changed_file"; then
    RUN_INTEGRATION_TESTS=true
  fi
done <<< "$CHANGED_FILES"

if [ "$RUN_BACKEND_VERIFICATION" = true ]; then

  # 부분 스테이징 경고
  if [ -n "$PARTIAL" ]; then
      echo "[warn] 부분 스테이징 감지 (테스트는 워킹 트리 기준으로 실행됨)"
      echo "$PARTIAL" | sed 's/^/  - /'
    fi

  # 스프링 테스트 (통합 테스트 제외)
  echo "[verify] test (exclude integration, requires-infra)"
  ./gradlew test -DexcludeTags=integration,requires-infra || { echo "[fail] test"; exit 1; }

  if [ "$RUN_INTEGRATION_TESTS" = true ]; then
    echo "[verify] test (include integration, exclude requires-infra)"
    ./gradlew test -DincludeTags=integration -DexcludeTags=requires-infra || { echo "[fail] integration test"; exit 1; }
  fi

  echo "[verify] SUCCESS"
else
  echo "[skip] no backend-related changes"
fi
