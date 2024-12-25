# 🚗 **카우머치 프로젝트 Back-end**

AI 기반 차량 사고 분석 및 견적 입찰 서비스 **백엔드** 레포지토리  
**📢 캡스톤 디자인 경진대회 최우수상 수상작** 🏆

[🎥 **데모 영상 보기**](https://youtu.be/81JoqTP4Jds?si=r36hSpss8QZXQKZV)

<br/>

## 📚 목차

- [인원 소개](#-인원-소개)
- [사용 기술 및 환경](#-사용-기술-및-환경)
- [프로젝트 소개](#-프로젝트-소개)
- [아키텍쳐 구성도](#-아키텍쳐-구성도)
- [ERD](#-ERD)
- [기술적 고민](#-기술적-고민)
- [향후 개발 및 개선 사항](#-향후-개발-및-개선-사항)

<br/>

## 🧑‍💻 인원 소개
|                                                                조영무                                                                |                                        정석현                                        |                                                                                                              
|:---------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------:|
| <img width="160px" src="https://avatars.githubusercontent.com/u/75081608?s=400&u=c4c22f3af10105e0fb18a9d346988e9403a533f6&v=4" /> | <img width="160px" src="https://avatars.githubusercontent.com/u/113079762?v=4" /> |
|                                               [@fprh13](https://github.com/fprh13)                                                |              [@jeongseockhyeon](https://github.com/jeongseockhyeon)               |
|                                                            인증, 견적, 입찰                                                             |                                     이미지, 커뮤니티                                     |

<br/>

## ⚒️ 사용 기술 및 환경
### ⚙️ Backend

| 기술 & 환경        | 버전    |
|----------------|-------|
| Java           | 17.0.9 |
| Spring Boot    | 3.2.4 |
| Gradle         | 8.7   |
| Spring Security | 6.2.3 |
| Qeurydsl       | 5.0.0 |
| Hibernate      | 6.4.4 |
| MySQL          | 8.0.33 |
| Redis          | 6.2.12 |

### 🚀 DevOps / Infra
| 기술 & 환경        |
|----------------|
| AWS EC2        |
| Route53        |
| S3             |
| CloudFront     |
| GitHub Actions |
| Docker         |
| Caddy Server   |

<br/>

## 📍 프로젝트 소개

[![image.jpg](https://i.postimg.cc/t7Gyx61g/image.jpg)](https://postimg.cc/Fkp2t7Vt)

AI 통해 사고 파손 부위와 손상 정도를 분석하고, 웹 기술을 접목하여 사고 처리 과정에서 발생하는 비용과 시간을 절약하는 사용자 친화적인 서비스를 제공합니다.

## 📄 아키텍쳐 구성도
[![image.jpg](https://i.postimg.cc/4yBqJhzM/image.jpg)](https://postimg.cc/Mffd5H3b)

## 📄 ERD
[![image.jpg](https://i.postimg.cc/9QJtF5ND/image.jpg)](https://postimg.cc/Hr87BKmm)

<br/>

## ✨ 백엔드 주요 기능

- **사용자 기능 및 인증 서비스**
  - Spring Security와 JWT를 사용한 인증 서비스 구축 및 소셜 로그인 기능
- **견적 기능**
  - S3에 차량 파손 사진을 업로드하는 기능
  - AI 분석 결과를 기반으로 차량 견적서를 등록하고 Enum 클래스로 정의된 공개 범위를 설정하여 관리하는 기능 제공
- **공업사 기능**
  - QueryDSL을 통해 AI 예상 수리 금액, 지역, 픽업 유무 등 다양한 조건으로 차량 견적서에 대한 상세 검색 기능 제공
- **입찰 기능**
  - 공업사는 수리를 희망하는 견적서에 신청, 사용자는 원하는 공업사를 선택하며 매칭되는 공개 입찰 기능 제공
- **커뮤니티 기능**
  - 사용자들이 게시글과 댓글을 통해 자유롭게 소통할 수 있는 커뮤니티 기능을 제공


<br/>

## 📁 **기술적 고민**

---

### **Update 진행 후 Id 값을 반환해도 되는가?**
영무님의 [블로그 정리 보기](https://fprh13.tistory.com/9)
- **문제 상황**  
  Update 요청에서 Id와 같은 필수 데이터를 반환하는 것이 CQS(Command Query Separation) 원칙을 위반할 가능성이 있어 바람직하지 않을 수 있다는 고민

- **해결 방안**
    - update의 `void` 응답에 집착하지 않고, 상황에 따라 Id와 같은 필수 데이터를 반환하는 것은 큰 문제가 되지 않는 것으로 결론

---

### **비밀번호 찾기 기능 개선**
영무님의 [블로그 정리 보기](https://velog.io/@fprh13/spring-boot-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EB%B3%80%EA%B2%BD-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%9D%B8%EC%A6%9D%EB%B2%88%ED%98%B8-%EA%B5%AC%ED%98%84)
- **문제 상황**  
  난수 기반 비밀번호 변경 방식에 대한 사용자 불편 사항 발생

- **해결 방안**
    - 여러 플랫폼의 비밀번호 찾기 프로세스를 분석
    - 인증번호와 JWT를 활용하여 즉시 비밀번호를 변경할 수 있는 프로세스를 구현
---

### **소셜 로그인 구현 의견 충돌**
영무님의 [블로그 정리 보기](https://velog.io/@fprh13/Spring-boot-spring-security-%EC%9E%90%EC%B2%B4-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%86%8C%EC%85%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EB%B0%B1%EC%97%94%EB%93%9C%EC%97%90%EC%84%9C-%EB%AA%A8%EB%91%90-%EC%B2%98%EB%A6%AC-%EC%8B%9C-%ED%9A%8C%EC%9B%90-%ED%83%88%ED%87%B4-%EC%B9%B4%EC%B9%B4%EC%98%A4-%EA%B5%AC%EA%B8%80-%EB%84%A4%EC%9D%B4%EB%B2%84-%EC%97%B0%EA%B2%B0-%EB%81%8A%EA%B8%B0)
- **문제 상황**  
  소셜 로그인 기능 제외를 제안받았으나, 백엔드 측에서는 비즈니스적으로 필요하다고 판단하여 의견 충돌 발생

- **해결 방안**
    - 모든 인증 과정을 백엔드에서 처리하여 프론트엔드에서 하이퍼링크 GET 요청만으로 소셜 로그인 절차를 진행할 수 있는 프로세스를 설계
---

### **모바일 크롬과 사파리 쿠키 문제**
- **문제 상황**  
  Vercel과 통신을 위해 Spring Boot를 HTTPS로 설정하면서 모바일 크롬과 사파리에서 쿠키 삽입이 안 되는 문제 발생.

- **해결 방안**
    - 문서를 통해 각 브라우저의 `SameSite` 속성이 어떻게 적용되는지 방식을 분석
    - 가비아에서 도메인을 구입해 프론트엔드와 백엔드의 서브 도메인을 일치시켜 문제 해결

---

### **PathVariable Validation 핸들링 문제**
영무님의 [블로그 정리 보기](https://velog.io/@fprh13/Spring-boot-PathVariable-값-validation-핸들러)
- **문제 상황**  
  `PathVariable` 값에 대한 Validation 핸들링이 제대로 작동하지 않음

- **해결 방안**
    - `ConstraintViolationException.class` 내부를 분석하여 문제를 해결

---

### **Validation 적용 순서 문제**
영무님의 [블로그 정리 보기](https://velog.io/@fprh13/Spring-boot-validation-순서-정하기)
- **문제 상황**  
    Validation 순서가 랜덤하게 처리되는 문제 발생

- **해결 방안**
    - `ValidationSequence` 인터페이스를 작성하여 Validation 순서를 정의

---


<br/>

## 🚀 **향후 개발 및 개선 사항**

- [x] **CI/CD 구축**
    - GitHub Actions를 활용하여 자동 빌드 및 배포 파이프라인을 설계하고 구현

- [ ] **리팩토링 및 테스트 코드 작성**
    - 주요 로직 및 서비스의 리팩토링을 통해 코드 가독성과 유지보수성을 향상
    - **단위 테스트(Unit Test)** 와 **통합 테스트(Integration Test)** 작성
