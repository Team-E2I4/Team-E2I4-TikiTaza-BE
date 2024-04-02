# Tiki Taza

![](https://i.imgur.com/2pnuCQb.png)

### 👉[티키타자 바로가기](https://www.tikitaza.com)

<br>

## ✨ 프로젝트 소개

### 🕹️ 누구나 쉽게 즐길 수 있는 웹 기반 타이핑 게임입니다!

1. **문장 게임** <br>
   영화 대사, 명언 등 다양한 문장이 주어집니다. <br>
   주어진 문장을 누구보다 빠르고 정확하게 입력하여 게임에서 승리하세요!
2. **코드 게임** <br>
   다양한 언어의 코드가 주어집니다. <br>
   주어진 코드를 누구보다 빠르고 정확하게 입력하여 게임에서 승리하세요!
3. **단어 게임** <br>
   화면의 단어들을 입력하여 점수를 획득하세요. <br>
   가장 많은 단어를 입력한 사람이 승자가 됩니다!

### 👥 프로젝트 멤버

|                                                                       박영재                                                                       |                                                                    김대휘                                                                     |
|:-----------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------:|
| <img width="100" alt="zero" src="https://github.com/Team-E2I4/Team-E2I4-TikiTaza-BE/assets/121790935/b2270459-a532-4dc8-96e2-69ecb7de2345"><br> | <img width="100" alt="hwi" src="https://github.com/Team-E2I4/Team-E2I4-TikiTaza-BE/assets/121790935/23456d3c-14a1-4874-9ddb-0d13ea730b44"> |
|                                             <center>[zerozae](https://github.com/park0jae)</center>                                             |                                         <center>[marooo326](https://github.com/marooo326)</center>                                         |

<br>

## 🔍 주요기능

- `실시간 게임` 상대방의 위치를 실시간으로 확인하며 긴장감 넘치는 게임을 즐겨보세요.
- `실시간 접속자` 누가 접속해있는지 확인하고 함께 게임을 즐겨보세요!
- `랭킹` 전체, 모드별 랭킹을 통해 순위를 확인해보세요!
- `초대 코드` 랜덤한 코드로 친구를 초대할 수 있어요!

<br>

## 📆 프로젝트 기간

- 1차 개발: **2024.02.02 ~ 2024.03.24**
- 2차 개발: **2024.04.01 ~ 진행중**

<br>

## 🛠️ 기술 스택

### 개발 환경

<p>
<img src="https://img.shields.io/badge/JAVA 17-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot 3-6DB33F?style=for-the-badge&logo=Spring boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security 6-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
</p>

<p>
<img src="https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/SockJS-231F20?style=for-the-badge&logo=SockJS&logoColor=white">
<img src="https://img.shields.io/badge/STOMP-231F20?style=for-the-badge&logo=STOMP&logoColor=white">
<img src="https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=Kafka&logoColor=white">
</p>

<p>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</p>

<p>
<img src="https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=AMAZON EC2&logoColor=white">
<img src="https://img.shields.io/badge/ECS-FF9900?style=for-the-badge&logo=AMAZON ECS&logoColor=white">
<img src="https://img.shields.io/badge/RDS-527FFF?style=for-the-badge&logo=AMAZON RDS&logoColor=white">
<img src="https://img.shields.io/badge/Elasticache-C925D1?style=for-the-badge&logo=AMAZON Elasticache&logoColor=white">
<img src="https://img.shields.io/badge/ROUTE53-8C4FFF?style=for-the-badge&logo=AMAZON ROUTE53&logoColor=white">
</p>

### 협업 도구

<p>
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
<img src="https://img.shields.io/badge/Github-000000?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
</p>

<br>

## 🏗️ 아키텍처

![](https://i.imgur.com/gcx91a8.png)

<br>

## ✏️ 기술적 선택

| **기능**          | **기술 선택 이유 및 근거**                                                                                                                                                                                                                                                                                         |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **게임방 리스트 조회**  | - Polling은 구현이 간단하지만, 실시간 반응을 위해 서버에 짧은 간격으로 요청을 반복해야 하며, 이로 인해 서버 부하가 증가할 수 있음<br>- Long Polling은 실시간성을 향상시킬 수 있으나, 구현 복잡성이 증가하며 서버 리소스 관리가 더 까다로워질 수 있음<br>- WebSocket의 경우 실시간성을 달성할 수 있으나, 게임방 리스트 조회 기능에서는 양방향 통신을 필요로 하지 않음<br>- **따라서 클라이언트와 서버 간의 단방향 통신을 위한 기술인 Server-Sent Events (SSE)를 채택하였음** |
| **게스트 관리**      | - 일회성 게스트는 MySQL과 같은 RDB에 저장될 필요가 없다고 판단<br>- Redis의 경우, MySql에 비해 접근 속도가 빠르고 다양한 데이터 형식을 저장할 수 있음<br>- 또한, 자체적으로 만료시간을 설정할 수 있음<br>- **따라서 Redis(운영환경에서는 Elaticache)를 활용하여 게스트를 관리하기로 결정**                                                                                                               |
| **실시간 게임 정보**   | - 실시간 게임 정보는 짧은 주기로 많은 삽입&업데이트 연산이 발생함<br>- Redis는 key를 이용해 빠른 접근이 가능하고, 싱글 스레드 구조로 인해 동시성 문제에 있어서도 상대적으로 안전<br>- **따라서 Redis(운영환경에서는 Elaticache)를 활용하여 실시간 게임 정보를 관리하기로 결정**                                                                                                                             |
| **웹소켓 메세지 브로커** | - 여러 대의 동시에 운영 될 시, 인메모리 브로커를 사용하면 브로커 간의 메세지 동기화가 이루어지지 않음<br>- **따라서 Kafka를 외부 메세지 브로커로 도입하여 모든 메세지가 중앙에서 처리될 수 있도록 구성**                                                                                                                                                                                |
| **랭킹**          | - 작업 실행 시간이 고정되어 있고 해당 시간에 실행되는 로직이 단순함을 고려하여, 프로젝트의 규모에 맞는 **Spring Scheduler**를 사용                                                                                                                                                                                                                      |

<br>
