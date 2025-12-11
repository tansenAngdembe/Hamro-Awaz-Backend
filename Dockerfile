FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY admin/target/  AWAZ_ADMIN.WAR

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "AWAZ_ADMIN.WAR"]
