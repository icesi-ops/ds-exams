# Use a base image with OpenJDK
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files to the working directory
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Build the application
RUN ./gradlew build

# Expose the port where your Java application will run (if needed)
EXPOSE 8000

# Define the command to run your Java application
CMD ["java", "-jar", "build/libs/your-artifact-id-1.0-SNAPSHOT.jar"]
