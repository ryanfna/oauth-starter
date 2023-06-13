FROM gradle:7.6.1-jdk17 AS gradleimage
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon --stacktrace

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=gradleimage /home/gradle/src/build/libs/*.jar /application.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker",  "-jar", "application.jar"]