# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package


# Runtime stage
FROM eclipse-temurin:21-jre-jammy

# Libraries required by JavaFX on Linux
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        libgtk-3-0 \
        libx11-6 \
        libxtst6 \
        libxi6 \
        libxrender1 \
        libxext6 \
        libgl1 \
        libasound2 \
        libfreetype6 \
        libfontconfig1 \
        libpango-1.0-0 \
        libcairo2 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the compiled application
COPY --from=build \
    /app/target/otp1_inclass-u.jar \
    /app/app.jar

# Copy application dependencies
COPY --from=build \
    /app/target/dependency \
    /app/lib

# Windows X server address
ENV DISPLAY=host.docker.internal:0.0

# Launch the Java application
ENTRYPOINT ["java", "-cp", "/app/app.jar:/app/lib/*", "Launcher"]
