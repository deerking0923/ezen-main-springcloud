FROM openjdk:17-ea-11-jdk-slim
ENV JAVA_OPTS="-Xmx128m -Xms128m"
VOLUME /tmp
COPY target/service-discovery-1.0.jar service-discovery.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar service-discovery.jar"]