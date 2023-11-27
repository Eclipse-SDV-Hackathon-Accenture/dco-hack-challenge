FROM amazoncorretto:17 AS app
COPY tracks-management-service/app/target/*.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
