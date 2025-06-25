FROM openjdk:21-jdk
WORKDIR /app
ADD target/food-ordering-dock.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
