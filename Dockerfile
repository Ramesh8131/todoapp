FROM openjdk:17-jdk-slim
WORKDIR /data
COPY target/todoapp.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]

