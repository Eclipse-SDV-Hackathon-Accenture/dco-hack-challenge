FROM amazoncorretto:17-al2-jdk AS app
COPY scenario-library-service/app/target/*.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
