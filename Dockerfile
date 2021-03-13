FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/proposta-0.0.1-SNAPSHOT.jar proposta.jar
ENTRYPOINT ["java","-jar","/proposta.jar"]
