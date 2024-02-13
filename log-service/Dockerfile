FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/log-service-0.1.jar LogService.jar
ENTRYPOINT ["java","-jar","LogService.jar"]