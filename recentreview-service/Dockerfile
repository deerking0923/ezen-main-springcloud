FROM openjdk:17-ea-11-jdk-slim
ENV JAVA_OPTS="-Xmx128m -Xms128m"
VOLUME /tmp
COPY target/recentreview-service-1.0.jar recentreview-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar recentreview-service.jar"]