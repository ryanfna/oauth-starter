FROM amazoncorretto:17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build --no-daemon

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]