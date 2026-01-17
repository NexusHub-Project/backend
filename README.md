🔮 ARCANE : League of Legends Analytics Platform
<p align="center"> <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"> <img src="https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.4.1"> <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"> <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger"> </p>

ARCANE은 라이엇 게임즈의 빅데이터를 실시간으로 수집하고 분석하여, 플레이어에게 단순한 전적 검색 이상의 가치를 제공하는 고성능 분석 플랫폼입니다. 독자적인 알고리즘을 통해 게임 기여도를 수치화하고, 최상위권 랭커 데이터를 기반으로 최적의 승리 전략을 제시합니다.

##🚀 Key Features
💎 Advanced Analytics (OurScore & TeamScore)

Performance Metric: 단순 KDA를 넘어 딜량, 시야, 오브젝트 관여도를 종합한 OurScore를 산출하여 개인의 실질 기여도를 측정합니다.

Match Evaluation: 팀 전체의 유기적인 플레이를 TeamScore로 객체화하여 승패의 핵심 원인을 분석합니다.

⚡ Real-time High-Tier Ranking

Zero-Downtime Update: Redis의 Atomic Swap(Temp-to-Real) 기법을 활용하여, 랭킹 업데이트 중에도 서비스 중단 없이 실시간 챌린저/그마/마스터 리스트를 제공합니다.

Pipelining Technique: 대량의 랭커 데이터를 Redis Pipeline을 통해 고속으로 동기화하여 지연 시간을 최소화했습니다.

🛡️ Strategic Guidance

Matchup Analysis: 특정 챔피언 간의 상대 승률과 상성 데이터를 분석하여 맞춤형 빌드를 추천합니다.

Position-based Statistics: 탑부터 서포터까지 각 포지션별 정교한 통계 지표를 제공합니다.

🛠 Technical Architecture
⚙️ Backend Engineering

Language: Java 21 (Latest LTS)

Framework: Spring Boot 3.4.1

Security: Spring Security & JWT 기반의 무상태 인증 체계

Data Persistence:

JPA (Hibernate): 객체 지향적 데이터 관리 및 변경 감지(Dirty Checking) 적용

MySQL 8.0: 소환사 정보 및 매치 히스토리 저장을 위한 관계형 DB

Redis: 실시간 랭킹 데이터 캐싱 및 패치 버전 관리 최적화

📡 Riot API Integration

Resilience: 429 Too Many Requests 대응을 위한 지수 백오프(Exponential Backoff) 기반 재시도 로직 구현

Efficiency: 라이엇 데이터 드래곤(Data Dragon) 패치 버전을 Redis에 캐싱하여 불필요한 네트워크 오버헤드 90% 이상 절감

📦 Tech Stack Summary
Category	Technology
Language	Java 21
Framework	Spring Boot 3.4.1
Database	MySQL 8.0, Redis
ORM	Spring Data JPA
Infrastructure	Docker, Docker Compose
API Spec	Swagger (OpenAPI 3.0)
Auth	Spring Security, JWT
🛠 Getting Started
Prerequisites

JDK 21

Docker & Docker Compose

Installation & Run

Repository Clone

Bash
git clone https://github.com/your-username/ARCANE.git
cd ARCANE/backend
Environment Setup src/main/resources/application.yml 파일에 본인의 Riot API Key를 설정합니다.

YAML
riot:
  api-key: YOUR_RIOT_API_KEY
Infrastructure Up

Bash
docker-compose up -d
Build & Run

Bash
./gradlew bootRun
📖 API Documentation
ARCANE은 Swagger를 통해 명확한 API 명세를 제공합니다. 서버 실행 후 아래 주소에서 확인 가능합니다.

Swagger UI: http://localhost:8080/swagger-ui/index.html

⚖️ Disclaimer
ARCANE isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or anyone officially involved in producing or managing League of Legends. League of Legends and Riot Games are trademarks or registered trademarks of Riot Games, Inc. League of Legends © Riot Games, Inc.
