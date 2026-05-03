# STAGE 1 — Compilar
# Usamos una imagen que ya tiene Maven y Java 17 instalados
FROM maven:3.9-eclipse-temurin-17 AS builder

# Creamos la carpeta /app y trabajamos desde ahí
WORKDIR /app

# Copiamos el pom.xml para que Maven sepa qué dependencias descargar
COPY pom.xml .

# Copiamos el código fuente
COPY src ./src

# Maven descarga las dependencias, compila el código y genera el JAR
RUN mvn clean package -DskipTests

# STAGE 2 — Ejecutar
# Imagen limpia con solo Java (JRE) para ejecutar el JAR, sin Maven ni herramientas de compilación
FROM eclipse-temurin:17-jre

# Creamos la carpeta /app y trabajamos desde ahí
WORKDIR /app

# Copiamos el JAR generado en el Stage 1 (builder) a esta imagen limpia
COPY --from=builder /app/target/*.jar app.jar

# Documentamos que la app corre en el puerto 8080
EXPOSE 8080

# Comando que se ejecuta cuando el contenedor arranca - inicia la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]