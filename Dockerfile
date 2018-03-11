FROM openjdk:8-jdk-alpine

ADD target/test-resource-server-0.0.1-SNAPSHOT.jar test-resource-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test-resource-server-0.0.1-SNAPSHOT.jar"]