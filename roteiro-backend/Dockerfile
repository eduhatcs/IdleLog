# syntax=docker/dockerfile:1

# ---------- build ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

# ---------- runtime ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/target/roteiro-backend-0.0.1-SNAPSHOT.jar app.jar
USER spring

# O Render define a porta em PORT (padrão 10000); a aplicação lê essa variável.
EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
