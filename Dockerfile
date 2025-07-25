FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests spring-boot:repackage

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN addgroup -S javaapp && adduser -S javauser -G javaapp && \
  chown -R javauser:javaapp /app

USER javauser

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]