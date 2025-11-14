# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9 AS build

WORKDIR /app

# Copiar configuración personalizada de Maven
COPY maven-settings.xml /usr/share/maven/conf/settings.xml

# Copiar solo pom.xml primero para aprovechar cache de Docker
COPY pom.xml .

# Descargar dependencias con configuración mejorada (se cachea si no cambia pom.xml)
RUN mvn dependency:go-offline -B -s /usr/share/maven/conf/settings.xml \
    -Dmaven.wagon.http.connectionTimeout=60000 \
    -Dmaven.wagon.http.readTimeout=120000 \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -Dmaven.wagon.http.retryHandler.count=3

# Copiar código fuente
COPY src src

# Compilar aplicación con configuración mejorada
RUN mvn clean package -DskipTests -B -s /usr/share/maven/conf/settings.xml \
    -Dmaven.wagon.http.connectionTimeout=60000 \
    -Dmaven.wagon.http.readTimeout=120000

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