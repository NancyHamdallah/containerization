# Build stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Install Maven manually
RUN apt-get update && \
    apt-get install -y maven

COPY . .
RUN mvn clean package

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/MySQLDBService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
