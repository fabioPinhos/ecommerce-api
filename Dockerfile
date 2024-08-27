# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-alpine

# Set the maintainer label
LABEL maintainer="your-email@example.com"

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
