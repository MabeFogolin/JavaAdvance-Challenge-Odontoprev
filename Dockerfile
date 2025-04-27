# Etapa 1: construir o JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o projeto
COPY pom.xml .
COPY src ./src

# Faz o build do projeto
RUN mvn clean package -DskipTests

# Etapa 2: imagem final só com o .jar
FROM openjdk:17-jdk
WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/N.I.B-0.0.1-SNAPSHOT.jar app.jar

# Define o comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]
