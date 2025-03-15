FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/community-service-1.0.jar community-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["java", "-jar", "community-service.jar"]
