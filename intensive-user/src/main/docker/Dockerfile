FROM openjdk:8
VOLUME /tmp
ADD intensive-user-1.0-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]