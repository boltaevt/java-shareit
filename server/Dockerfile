FROM amazoncorretto:11
COPY server/target/*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/app.jar", "--server.port=9090"]