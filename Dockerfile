# # -------- Stage 1: Build JAR using Maven --------
# FROM maven:3.9.6-eclipse-temurin-17 AS build

# WORKDIR /app

# # Copy all project files
# COPY . .

# # Build the jar file
# RUN mvn clean package -DskipTests

# # -------- Stage 2: Run the application --------
# FROM eclipse-temurin:17-jdk-jammy

FROM openjdk:17-jdk-slim

WORKDIR /data

# Copy jar from build stage
#COPY --from=build /app/target/*.jar app.jar
COPY target/*.jar app.jar

# Expose the Spring Boot default port
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "app.jar"]
