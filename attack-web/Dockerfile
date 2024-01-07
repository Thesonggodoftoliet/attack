FROM openjdk:16

EXPOSE 8080

ADD attck-0.9.0-SNAPSHOT.jar app.jar

RUN bash -c 'touch /app.jar'

ENTERYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]