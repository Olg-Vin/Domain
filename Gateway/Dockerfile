FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests
FROM amazoncorretto:21 AS runtime
WORKDIR /app
COPY --from=build /app/target/Gateway-1.0.jar /app/Gateway-1.0.jar
CMD ["java", "-jar", "Gateway-1.0.jar"]