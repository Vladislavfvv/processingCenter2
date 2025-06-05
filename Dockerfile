FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/processingcenter-*.jar app.jar
EXPOSE 8050
ENTRYPOINT ["java", "-jar", "app.jar"]
