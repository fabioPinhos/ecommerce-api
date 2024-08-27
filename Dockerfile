# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-oracle

RUN mkdir -p /opt/app

# Set the working directory inside the container
WORKDIR /opt/app

# Copy the JAR file from the target directory to the container
COPY ./target/*.jar /opt/app/app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
