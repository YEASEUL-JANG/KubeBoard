FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/deployment-service-0.1.jar DeploymentService.jar
COPY config /root/.kube/config
ENTRYPOINT ["java","-jar","DeploymentService.jar"]