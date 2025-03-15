FROM openjdk:17-ea-11-jdk-slim
ENV JAVA_OPTS="-Xmx128m -Xms128m"
VOLUME /tmp
COPY apiEncryptionKey.jks apiEncryptionKey.jks
COPY target/config-service-1.0.jar ConfigServer.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ConfigServer.jar"]
