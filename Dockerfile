# build
FROM gradle:8.5.0-jdk17-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# run
FROM eclipse-temurin:17-jdk-jammy AS runner

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]