FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/user-service-1.0.jar user-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["java", "-jar", "user-service.jar"]
