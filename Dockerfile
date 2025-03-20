FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
