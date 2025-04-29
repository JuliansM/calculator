FROM eclipse-temurin:21-jdk as build

WORKDIR /app

COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle
COPY src /app/src

RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
