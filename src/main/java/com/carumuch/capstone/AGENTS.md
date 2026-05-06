# Source Tree Guide

## 적용 범위

이 지침은 `src/main/java/com/carumuch/capstone` 이하에서 작업할 때 적용합니다.

루트 `AGENTS.md`를 그대로 상속하면서, Java 프로덕션 코드에 필요한 판단 기준만 추가합니다.

---

## 기본 원칙

1. 새 기능은 가능하면 `identity`, `damage`, `estimate`, `bodyshop` 같은 리팩터링된 컨텍스트에 둡니다.
2. 구조와 계층 책임의 상세 기준은 `docs/code-style.md`를 따릅니다.

---

## 변경 판단 기준

- 새 코드를 추가할 때는 먼저 같은 컨텍스트의 기존 계층 구조를 따라갑니다.
- 레거시 패턴과 리팩터링 패턴이 함께 보이면, 신규 코드는 리팩터링 패턴을 우선 선택합니다.
- 공통 로직이 필요해도 바로 `common.legacy`로 넣지 않습니다.

---

## 테스트

- 프로덕션 코드를 수정하면 대응되는 테스트 위치를 함께 확인합니다.
- 검증은 저장소 기준인 `./scripts/verify.sh`를 따릅니다.
- 리뷰가 필요할 때는 `docs/code-review.md`를 참고합니다.
