FROM openjdk:17-jdk-slim

# Cria uma pasta dentro do container
WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
