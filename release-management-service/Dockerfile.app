FROM amazoncorretto:17-al2-jdk AS app
COPY release-management-service/app/target/*.jar /app/app.jar
COPY release-management-service/app/tls /app/tls
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
