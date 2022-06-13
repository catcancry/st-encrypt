FROM openjdk:8-alpine3.9


RUN mkdir -p /spring-boot-example-server

WORKDIR /spring-boot-example-server

EXPOSE 8088

ADD ./target/spring-boot-example-server-1.0.jar ./app.jar

CMD sleep 30;java -Djava.security.egd=file:/dev/./urandom -jar app.jar