FROM openjdk:8-jdk-alpine
MAINTAINER cjw
ADD COLLECT-0.0.1.jar COLLECT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","COLLECT.jar"]
