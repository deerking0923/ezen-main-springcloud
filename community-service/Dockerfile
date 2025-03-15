FROM openjdk:17-ea-11-jdk-slim
ENV JAVA_OPTS="-Xmx128m -Xms128m"
VOLUME /tmp
COPY target/community-service-1.0.jar community-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar community-service.jar"]