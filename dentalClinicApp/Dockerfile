# Simple Dockerfile using pre-built JAR
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the pre-built jar (build it first with: mvn clean package -DskipTests)
COPY target/dental-clinic-app-0.0.3.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
