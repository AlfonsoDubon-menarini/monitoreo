# ─── ETAPA 1: Compilación y Construcción ──────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias primero para aprovechar el caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el JAR ejecutable saltando los tests (ya validados en CI)
COPY src ./src
RUN mvn clean package -DskipTests

# ─── ETAPA 2: Entorno de Ejecución Seguro (Hardening) ─────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear un usuario de sistema sin privilegios para mitigar riesgos de seguridad
RUN addgroup -S monitorgroup && adduser -S monitoruser -G monitorgroup
USER monitoruser

# Copiar únicamente el artefacto construido desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto interno de la aplicación Spring Boot
EXPOSE 8080

# Comando de arranque optimizado para entornos de contenedores
ENTRYPOINT ["java", "-jar", "app.jar"]
