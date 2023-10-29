FROM openjdk:17-alpine

ARG PROFILE

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]