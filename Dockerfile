# Stage 1: Build the application
FROM eclipse-temurin:21 AS builder
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
# Download dependencies to cache them
RUN ./mvnw dependency:go-offline -B

# Copy the source code and build the application
COPY src/ src/
RUN ./mvnw package -DskipTests -B

# Stage 2: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Run as non-root user for security
RUN addgroup -S sgpi && adduser -S sgpi -G sgpi
USER sgpi

COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
