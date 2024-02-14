FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/service-service-0.1.jar ServiceService.jar
COPY config /root/.kube/config
ENTRYPOINT ["java","-jar","ServiceService.jar"]