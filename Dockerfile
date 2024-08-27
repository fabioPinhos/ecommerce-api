# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl

# Copy the JAR file from the target directory to the container
COPY target/ecommerce-api-1.0-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
