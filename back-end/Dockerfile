# Build stage
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn/ .mvn

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

# Development stage
FROM eclipse-temurin:17-jdk-jammy AS development
WORKDIR /workspace

COPY --from=builder /workspace/ .

EXPOSE 8080
EXPOSE 5005

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=dev", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]

# Production stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]