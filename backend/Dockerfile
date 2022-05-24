FROM openjdk:17-oracle

ARG MAVEN_VERSION

WORKDIR /app

COPY ./target/backend-${MAVEN_VERSION}.jar /app/final.jar

EXPOSE 8080

ENTRYPOINT [ "java" ]
CMD [ "-jar", "/app/final.jar" ]
