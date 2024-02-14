FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/user-service-0.1.jar UserService.jar
COPY config /root/.kube/config
ENTRYPOINT ["java","-jar","UserService.jar"]