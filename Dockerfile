# --- Stage 1: Build the React Frontend ---
FROM node:20-slim AS frontend-build
WORKDIR /frontend

# Use JSON array syntax for paths with spaces - this is the most compatible way
COPY ["interview ui poc ts/my-react-app/package.json", "interview ui poc ts/my-react-app/package-lock.json*", "./"]
RUN npm install
COPY ["interview ui poc ts/my-react-app/", "./"]
RUN npm run build

# --- Stage 2: Build the Spring Boot Backend ---
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY ["interview api POC TS/pom.xml", "./"]
RUN mvn dependency:go-offline -B
COPY ["interview api POC TS/src", "./src/"]

# Copy React build into Spring Boot static resources
COPY --from=frontend-build /frontend/dist ./src/main/resources/static/

RUN mvn clean package -DskipTests

# --- Stage 3: Final Production Image ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
