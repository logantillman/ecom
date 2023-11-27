FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

ARG dbPassword
ENV dbPassword $dbPassword

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=\"-Dspring.profiles.active=docker\""]