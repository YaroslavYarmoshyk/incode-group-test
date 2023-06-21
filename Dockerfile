FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# First, copy only the pom.xml file and resolve dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Next, copy the source code and build the application
COPY src/ ./src/
RUN mvn package --no-transfer-progress -DskipTests=true

FROM openjdk:11.0.11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ./incode-group-test.jar
ENTRYPOINT ["java", "-jar", "/app/incode-group-test.jar"]
