FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY . /app
RUN mvn package
CMD ["java", "-jar", "target/OTP1_inclassAssignment3.jar"]