FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/pod-service-0.1.jar PodService.jar
COPY config /root/.kube/config
ENTRYPOINT ["java","-jar","PodService.jar"]