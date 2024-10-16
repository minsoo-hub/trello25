![header](https://capsule-render.vercel.app/api?type=venom&height=199&text=Trello%20project&textBg=false&fontColor=00fa9a)

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
- [커밋 컨벤션](https://teamsparta.notion.site/Github-Rules-3f66a7e19eea4591adebe70af9cfaf1d)

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
└─src
    ├─main
       ├─java
          └─com
              └─trello25
                  ├─client
                  │  └─slack
                  ├─config
                  ├─domain
                  │  ├─attachment
                  │  │  ├─dto
                  │  │  │  └─response
                  │  │  ├─entity
                  │  │  └─repository
                  │  ├─auth
                  │  │  ├─annotation
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  │  ├─request
                  │  │  │  └─response
                  │  │  └─service
                  │  ├─board
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  │  ├─request
                  │  │  │  └─response
                  │  │  ├─entity
                  │  │  ├─enums
                  │  │  ├─repository
                  │  │  └─service
                  │  ├─card
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  │  ├─request
                  │  │  │  └─response
                  │  │  ├─entity
                  │  │  ├─repository
                  │  │  └─service
                  │  ├─cardactive
                  │  │  ├─actiontype
                  │  │  ├─dto
                  │  │  ├─entity
                  │  │  └─repository
                  │  ├─comment
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  │  ├─request
                  │  │  │  └─response
                  │  │  ├─entity
                  │  │  ├─repository
                  │  │  └─service
                  │  ├─common
                  │  │  └─entity
                  │  ├─kanban
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  │  ├─request
                  │  │  │  └─response
                  │  │  ├─entity
                  │  │  ├─repository
                  │  │  └─service
                  │  ├─kanbanposition
                  │  │  ├─entity
                  │  │  ├─repository
                  │  │  └─service
                  │  ├─manager
                  │  │  ├─entity
                  │  │  └─repository
                  │  ├─member
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  ├─entity
                  │  │  ├─repository
                  │  │  └─service
                  │  ├─user
                  │  │  ├─controller
                  │  │  ├─dto
                  │  │  │  ├─request
                  │  │  │  └─response
                  │  │  ├─entity
                  │  │  ├─enums
                  │  │  ├─repository
                  │  │  └─service
                  │  └─workspace
                  │      ├─controller
                  │      ├─dto
                  │      ├─entity
                  │      ├─repository
                  │      └─service
                  └─exception

```

<br>

## 4. 역할 분담

### 김동규

- 시연영상 제작

- **기능**
  - 댓글 CRUD, 칸반 CRUD, 슬랙알림, DB 인덱싱
<br>

### 이유진

- ppt 제작 및 발표

- **기능**
  - 보드 CRUD, 이미지 업로드

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
  - workspace, member CRUD
<br>

### 최원
- **기능**
  - 유저관리 :회원가입, 로그인, 스프링 시큐리티, 유저검색 
<br>

## 5. 개발 기간 및 작업 관리

### 개발 기간

- 전체 개발 기간 : 2024-10-14~ 2024-10-18
- 기능 구현 : 2024-10-14 ~ 2024-10-18
- 발표자료 준비 : 2024-10-17 ~ 2024-10-18

<br>

### 작업 관리

- GitHub Projects와 slack을 사용하여 진행 상황을 공유했습니다.
- 매일 회의를 진행하며 작업 진행도와 방향성에 대한 고민을 나누었습니다.

<br>

## 6. ERD

![ERD (1)](https://github.com/user-attachments/assets/aea556cd-f852-436e-905c-6be926908eb4)



## 7. API 명세

![image (1)](https://github.com/user-attachments/assets/71b62f74-a094-41ee-af91-77ed2a33f0f4)

<br>

## 8. 프로젝트 후기

### 나민수

기능 구현에 어려움이 많았지만, 이번 팀프로젝트 하면서 팀원분들 덕분에 많이 배우고 얻어가는 것 같아서 좋습니다.

<br>

### 이유진

기간 내 기능 구현과 깃 merge시 충돌이 자주 일어나 깃 사용에서 약간 어려움이 있었습니다. 기간이 짧아서 육체적으로 많이 힘들었지만 팀원들과 원활한 소통 덕분에 프로젝트를 마무리 할 수 있었습니다.

<br>

### 김동규

좋은 팀원분들과 프로젝트를 함께 할 수 있어 행복했습니다. 

<br>

### 송민지
같은 결과같이라도 다른 구현 방식을 배울 수 있었습니다. 배울점이 많아서 기분이 좋습니다.
 
<br>

### 최원

유능한 팀원분들 덕분에 배워가는 것도 많았습니다. 동시성제어를 시간 안에 끝내지 못한부분이 너무 아쉽습니다.





















