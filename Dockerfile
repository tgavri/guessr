
FROM maven:3.9.9-eclipse-temurin-21 AS build

RUN apt-get update && apt-get install -y wget \
    && wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -O /usr/local/bin/wait-for-it \
    && chmod +x /usr/local/bin/wait-for-it

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

RUN apk update && apk add --no-cache bash wget

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY --from=build /usr/local/bin/wait-for-it /usr/local/bin/wait-for-it

EXPOSE 8080

ENTRYPOINT ["wait-for-it", "mysql:3306", "--", "java", "-jar", "app.jar"]
