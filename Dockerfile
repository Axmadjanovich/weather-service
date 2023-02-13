FROM openjdk:17-jre-alpine
COPY ./target/reactive-r2dbc-todo-list-app-1.0.0-SNAPSHOT.jar /usr/src/weather-service/
WORKDIR /usr/src/reactive-r2dbc-todo-list-application
CMD ["java", "-jar", "reactive-r2dbc-todo-list-app-1.0.0-SNAPSHOT.jar"]
