FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
ENV JAVA_OPTS="-Xmx128m -Xms128m"
COPY target/apigateway-service-1.0.jar ApigatewayService.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ApigatewayService.jar"]
