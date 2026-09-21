FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY . /app
RUN mvn package
CMD ["java", "-jar", "target/otp1_inclass-u.jar"]