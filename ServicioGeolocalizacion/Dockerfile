# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9 AS build

WORKDIR /app

# Copiar solo pom.xml primero para aprovechar cache de Docker
COPY pom.xml .

# Descargar dependencias (se cachea si no cambia pom.xml)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src src

# Compilar aplicación
RUN mvn clean package -DskipTests

# Imagen de runtime más pequeña
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Configurar JVM para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Exponer puerto
EXPOSE 8080

# Punto de entrada con configuración optimizada para contenedores
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]