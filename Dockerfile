#Angular
FROM node:20-alpine AS frontend-build
WORKDIR /app
COPY src/main/frontend/ ./frontend/
WORKDIR /app/frontend
RUN npm install
RUN npm run build --prod

#Spring Boot
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY . .
COPY --from=frontend-build /app/frontend/dist /app/src/main/resources/static
RUN mvn clean package -DskipTests

#Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]