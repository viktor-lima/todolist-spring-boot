FROM ubuntu:latest as build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
RUN apt-get install maven -y

WORKDIR /app

COPY . .

RUN mvn clean install

# segundo estágio
FROM ubuntu:latest

RUN apt-get update
RUN apt-get install openjdk-21-jre -y

WORKDIR /app

COPY --from=build /app/target/todolist-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]