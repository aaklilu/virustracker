FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR application
ARG JAR_FILE=build/libs/tvt_bot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} tvt_bot-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar tvt_bot-0.0.1-SNAPSHOT.jar extract

FROM adoptopenjdk:11-jre-hotspot
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/resources/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]