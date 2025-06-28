# --- 1단계: 빌드(Build) 환경 ---
# Java 21과 Gradle 도구를 포함한 베이스 이미지를 사용합니다.
FROM eclipse-temurin:21-jdk-jammy AS builder

# 컨테이너 내의 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# Gradle 래퍼 파일들을 먼저 복사하여 빌드 캐시를 활용합니다.
COPY gradlew .
COPY gradle ./gradle

# 의존성 관련 파일을 먼저 복사하여 별도로 다운로드합니다.
# 이렇게 하면 소스 코드가 변경되어도 매번 의존성을 새로 받지 않아 효율적입니다.
COPY build.gradle .
COPY settings.gradle .
RUN ./gradlew dependencies

# 프로젝트의 전체 소스 코드를 복사합니다.
COPY src ./src

# Gradle을 사용하여 프로젝트를 빌드하고, 실행 가능한 .jar 파일을 생성합니다.
RUN ./gradlew bootJar

# --- 2단계: 최종 실행(Runtime) 환경 ---
# 실제 서버 실행에는 JDK 전체가 필요 없으므로, 더 가벼운 JRE 이미지를 사용합니다.
FROM eclipse-temurin:21-jre-jammy

# 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 1단계(builder)에서 생성된 실행 가능한 .jar 파일을 최종 이미지로 복사합니다.
COPY --from=builder /app/build/libs/*.jar app.jar

# 이 컨테이너가 시작될 때, app.jar 파일을 실행하라는 명령입니다.
ENTRYPOINT ["java", "-jar", "app.jar"]