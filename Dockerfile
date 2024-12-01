# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Download the wait-for-it script
RUN apt-get update && apt-get install -y wget \
    && wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -O /usr/local/bin/wait-for-it \
    && chmod +x /usr/local/bin/wait-for-it

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jdk-alpine

# Install bash and wget in the runtime image
RUN apk update && apk add --no-cache bash wget

# Set the working directory in the runtime image
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the wait-for-it script from the build image
COPY --from=build /usr/local/bin/wait-for-it /usr/local/bin/wait-for-it

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["wait-for-it", "mysql:3306", "--", "java", "-jar", "app.jar"]
