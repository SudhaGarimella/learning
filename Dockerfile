FROM openjdk:8-alpine
WORKDIR /usr/share/java
COPY target/demo-0.0.1-SNAPSHOT.jar mdl-connector-1.0.jar

ENTRYPOINT ["java", "-jar", "/usr/share/java/mdl-connector-1.0.jar"]
