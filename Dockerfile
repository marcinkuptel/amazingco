FROM openjdk:8-jdk-alpine

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/amazing-co/amazing-co.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/amazing-co/amazing-co.jar
