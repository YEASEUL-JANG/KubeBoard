FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/deployment-service-0.1.jar DeploymentService.jar
ENTRYPOINT ["java","-jar","DeploymentService.jar"]