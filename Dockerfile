# It's needed to have the Dockerfile inside the root folder into the project
FROM gradle:7-jdk-alpine AS build
# Copy all files to the stage
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:8-jre-slim
ARG VERSION
RUN echo 'Version from docker build command '$VERSION
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/clab-docker-demo-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=kubernetes", "-jar", "/app/spring-boot-application.jar"]