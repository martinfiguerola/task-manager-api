# 1. Base image: lightweight image with Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# 2. Working directory: create /app folder inside the container
WORKDIR /app

# 3. Copy: bring the .jar from our machine into the container
COPY target/task-manager-0.0.1-SNAPSHOT.jar app.jar

# 4. Expose the port the app runs on
EXPOSE 8080

# 5. Startup command: tell the container what to do when it starts
ENTRYPOINT ["java", "-jar", "app.jar"]