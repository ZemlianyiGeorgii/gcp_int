FROM maven:3.6-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
FROM adoptopenjdk/openjdk11:alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/interview-*.jar /interview.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/interview.jar"]

