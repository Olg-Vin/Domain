FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests
FROM amazoncorretto:21 AS runtime
WORKDIR /app
COPY --from=build /app/target/Domain-service-1.0-SNAPSHOT.jar /app/Domain-service.jar
CMD ["java", "-jar", "Domain-service.jar"]