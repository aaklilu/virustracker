FROM gradle:6.3-jdk8 as builder
WORKDIR /workspace/app
VOLUME /tmp

COPY gradle $APP_HOME/gradle
RUN gradle build || return 0
COPY . .
RUN gradle bootJar

RUN mkdir -p build/dependency && (cd build/dependency; java -Djarmode=layertools -jar ../libs/*.jar extract)

FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=builder ${DEPENDENCY}/dependencies/ ./
COPY --from=builder ${DEPENDENCY}/snapshot-dependencies/ ./
COPY --from=builder ${DEPENDENCY}/resources/ ./
RUN true
COPY --from=builder ${DEPENDENCY} ./
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "org.springframework.boot.loader.JarLauncher"]