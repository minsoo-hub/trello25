![header](https://capsule-render.vercel.app/api?type=venom&height=199&text=오늘도%20밤새조&textBg=false)

# Trello Project

## 프로젝트 소개

- 사내 일정관리 프로젝트

<br>

## 팀원 구성


| **나민수**                                       | **이유진**                                        | **김동규**                              | **송민지**                                | **최원**                                    |
| ------------------------------------------------ | :------------------------------------------------ | :-------------------------------------- | :---------------------------------------- | :------------------------------------------ |
| [@minsoo-hub](https://github.com/minsoo-hub) | [@pringles1234](https://github.com/pringles1234) | [@bronbe](https://github.com/bronbe) | [@mj-song00](https://github.com/mj-song00) | [ @hoi1999](https://github.com/Choi1999) |

<br>

## 1. 개발 환경

- Back-end :  ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)![SQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)![POSTMAN](https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
- 버전 및 이슈관리 : ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) ![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
- 협업 툴 : ![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
- 디자인 : ![Excalidraw](https://img.shields.io/badge/excalidraw-%23F24E1E.svg?style=for-the-badge&logo=excalidraw&logoColor=white)
- [커밋 컨벤션](https://www.notion.so/teamsparta/Github-Rules-cd0d76a9b1614cdc8a5fea2210cd8b3a)

<br>

## 2. 브랜치 전략

### 브랜치 전략

- Git-flow 전략을 기반으로 main, develop 브랜치와 feature 보조 브랜치를 운용했습니다.
- main, develop, Feat 브랜치로 나누어 개발을 하였습니다.
  - **main** 브랜치는 배포 단계에서만 사용하는 브랜치입니다.
  - **develop** 브랜치는 개발 단계에서 git-flow의 master 역할을 하는 브랜치입니다.
  - **Feat** 브랜치는 기능 단위로 독립적인 개발 환경을 위하여 사용하고 merge 후 각 브랜치를 삭제해주었습니다.

<br>

## 3. 프로젝트 구조

```
|-- main
    |-- java
        |-- com
            |-- sparta
                |-- outsourcing
                    |-- aop
                        |-- apidocs
                        |-- LogLevel.java
                        |-- LogLevelController.java
                        |-- LogUtility.java
                        |-- OrderLoggingAspect.java
                    |-- domain
                        |-- common
                            |-- entity
                                |-- Timestamped.java
                        |-- menu
                            |-- controller
                                |-- MenuController.java
                            |-- dto
                                |-- request
                                    |-- CreateMenuRequestDto.java
                                    |-- UpdateMenuRequestDto.java
                                |-- response
                                    |-- CreateMenuResponseDto.java
                                    |-- UpdateMenuResponseDto.java
                            |-- entity
                                |-- Menu.java
                            |-- repository
                                |-- MenuRepository.java
                            |-- service
                                |-- MenuService.java
                        |-- order
                            |-- controller
                                |-- OrderController.java
                            |-- dto
                                |-- request
                                    |-- OrderRequestDto.java
                                |-- response
                                    |-- OrderResponseDto.java
                            |-- entity
                                |-- Orders.java
                            |-- enums
                                |-- OrderStatus.java
                            |-- repository
                                |-- OrderRepository.java
                            |-- service
                                |-- OrderService.java
                        |-- review
                            |-- controller
                                |-- ReviewController.java
                            |-- dto
                                |-- ReviewRequestDTO.java
                                |-- ReviewResponseDTO.java
                            |-- entity
                                |-- Review.java
                            |-- repository
                                |-- ReviewRepository.java
                            |-- service
                                |-- ReviewService.java
                        |-- store
                            |-- controller
                                |-- StoreController.java
                            |-- dto
                                |-- request
                                    |-- StoreRequestDto.java
                                |-- response
                                    |-- StoreResponseDto.java
                            |-- entity
                                |-- Store.java
                            |-- repository
                                |-- StoreRepository.java
                            |-- service
                                |-- StoreService.java
                        |-- user
                            |-- config
                                |-- annotation
                                    |-- Auth.java
                                |-- auth
                                    |-- AuthUserArgumentResolver.java
                                    |-- FilterConfig.java
                                    |-- JwtFilter.java
                                    |-- JwtUtil.java
                                    |-- WebConfig.java
                                |-- error
                                    |-- AuthErrorCode.java
                                    |-- EroorCode.java
                                |-- JpaConfig.java
                                |-- password
                                    |-- PasswordEncoder.java
                            |-- controller
                                |-- UserController.java
                            |-- dto
                                |-- AuthUser.java
                                |-- request
                                    |-- SignInRequestDto.java
                                    |-- SignUpRequestDto.java
                                    |-- UserDeleteDto.java
                            |-- entity
                                |-- User.java
                            |-- enums
                                |-- UserRole.java
                            |-- repository
                                |-- UserRepository.java
                            |-- service
                                |-- UserService.java
                    |-- exception
                        |-- ApplicationException.java
                        |-- ErrorCode.java
                        |-- ErrorResponse.java
                        |-- GlobalExceptionHandler.java
                    |-- OutsourcingApplication.java
    |-- resources
|-- test
    |-- java
        |-- com
            |-- sparta
                |-- outsourcing
                    |-- domain
                        |-- menu
                            |-- controller
                            |-- service
                        |-- order
                            |-- controller
                            |-- service
                        |-- review
                            |-- service
                        |-- store
                            |-- controller
                            |-- service
                        |-- user
                            |-- controller
                            |-- service
                    |-- OutsourcingApplicationTests.java
    |-- java1
        |-- com
            |-- sparta
                |-- outsourcing
                    |-- domain
                        |-- menu
                            |-- controller
                            |-- service
                        |-- store
                            |-- controller
                            |-- service
                        |-- user
                            |-- service
```

<br>

## 4. 역할 분담

### 김동규

- 시연영상 제작

- **기능**
  - 댓글 CRUD, 슬랙알림, DB 인덱싱
<br>

### 이유진

- ppt 제작 및 발표

- **기능**
  - 보드 CRUD, 이미지 업로드

<br>

### 나민수

- 팀장
- ppt 제작

- **기능**
  - 카드 CRUD

<br>

### 송민지
- ppt 제작 
- **기능**
  - workspace, member CRUD
<br>

### 최원
- **기능**
  - 유저관리 :회원가입, 로그인, 스프링 시큐리티, 저검색 
<br>

## 5. 개발 기간 및 작업 관리

### 개발 기간

- 전체 개발 기간 : 2024-10-14~ 2024-10-18
- 기능 구현 : 2024-09-14 ~ 2024-09-18
- 발표자료 준비 : 2024-10-17 ~ 2024-10-18

<br>

### 작업 관리

- GitHub Projects와 slack을 사용하여 진행 상황을 공유했습니다.
- 매일 회의를 진행하며 작업 진행도와 방향성에 대한 고민을 나누었습니다.

<br>

## 6. ERD

![erd](https://files.slack.com/files-pri/T06B9PCLY1E-F07SCUNC1T5/erd__1_.png)




## 7. API 명세

![API](https://files.slack.com/files-pri/T06B9PCLY1E-F07T1RDH0BA/image.png)


<br>

## 8. 프로젝트 후기

### 나민수


<br>

### 이유진

기간 내 기능 구현과 테스트 코드까지 작성하느라 리팩토링과 트러블 슈팅 기록등을 하지 못해서 아쉬움이 남지만 테스트코드 부분에서 많이 배우고 팀원들이 많이 도와주어서 무사히 프로젝트를 마무리한 것 같습니다.

<br>

### 김동현



<br>

### 송민지

 
<br>

### 최원























