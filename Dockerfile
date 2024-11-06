FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY build/libs/capstone-1.0.jar Capstone.jar
ENTRYPOINT ["java", "-jar", "Capstone.jar"]
