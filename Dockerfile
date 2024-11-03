FROM openjdk:17-slim  AS builder
## RUN  apk install -y findutils && apt-get install -y xargs
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17

# Datadog Java Agent 추가
COPY dd-java-agent.jar /usr/agent/dd-java-agent.jar

# 빌드 이미지에서 app.jar 파일을 복사
COPY --from=builder build/libs/*.jar app.jar

# /tmp를 볼륨으로 지정
VOLUME /tmp

# app.jar 실행
ENTRYPOINT ["java", "-javaagent:/usr/agent/dd-java-agent.jar", "-Ddd.agent.host=localhost", "-Ddd.profiling.enabled=true", "-XX:FlightRecorderOptions=stackdepth=256", "-Ddd.logs.injection=true", "-Ddd.service=discovery-api", "-Ddd.env=prod", "-Dspring.profiles.active=production", "-jar", "/app.jar"]