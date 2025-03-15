FROM openjdk:17-ea-11-jdk-slim
ENV JAVA_OPTS="-Xmx128m -Xms128m"
VOLUME /tmp
COPY target/user-service-1.0.jar user-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar user-service.jar"]