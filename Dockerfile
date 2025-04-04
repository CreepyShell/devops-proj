FROM openjdk:17-jdk
WORKDIR /app

#application port
ENV APP_PORT=8080
EXPOSE $APP_PORT

COPY target/SwingApp-1.0-SNAPSHOT.jar SwingApp-1.0-SNAPSHOT.jar
CMD java -jar SwingApp-1.0-SNAPSHOT.jar "$APP_PORT"