FROM openjdk:17
VOLUME /tmp 
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
expose 8081 # Container में port declare करना
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["--server.port=8081"]

