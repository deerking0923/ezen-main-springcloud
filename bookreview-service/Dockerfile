FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/bookreview-service-1.0.jar bookreview-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["java", "-jar", "bookreview-service.jar"]
