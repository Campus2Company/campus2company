# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app



# Copy Maven wrapper and pom.xml
COPY university-admin-service/mvnw .
COPY university-admin-service/.mvn .mvn
COPY university-admin-service/pom.xml .

COPY campus2company-lib/pom.xml ./campus2company-lib/pom.xml
COPY campus2company-lib/src ./campus2company-lib/src
RUN cd campus2company-lib && mvn install -DskipTests -B


# Download dependencies (cached layer)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw && ./mvnw dependency:go-offline -B
# Copy source code
COPY university-admin-service/src src
# Build the application
RUN ./mvnw package -DskipTests -B
# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
# Create non-root user
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
# Copy the built jar
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]