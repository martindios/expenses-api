FROM openjdk:21-jdk-slim

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

COPY src src

RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/expenses-api-0.0.1-SNAPSHOT.jar"]
