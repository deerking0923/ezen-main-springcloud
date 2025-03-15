FROM openjdk:17-ea-11-jdk-slim
ENV JAVA_OPTS="-Xmx128m -Xms128m"
VOLUME /tmp
COPY target/mylibrary-service-1.0.jar mylibrary-service.jar
COPY src/main/resources/.env .env
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar mylibrary-service.jar"]