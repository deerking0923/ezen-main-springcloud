FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/mylibrary-service-1.0.jar mylibrary-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["java", "-jar", "mylibrary-service.jar"]