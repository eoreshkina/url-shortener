FROM amazoncorretto:21-alpine

ENV SPRING_PROFILES_ACTIVE=local

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app/app.jar"]
