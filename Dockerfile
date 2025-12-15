FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY admin/target/HAMRO_AWAZ.war app.war

EXPOSE 9081

ENTRYPOINT ["java", "-jar", "/app/app.war"]
