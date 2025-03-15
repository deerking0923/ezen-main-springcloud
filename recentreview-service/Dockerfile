FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/recentreview-service-1.0.jar recentreview-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["java", "-jar", "recentreview-service.jar"]
