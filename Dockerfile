# Etapa 1: Build da aplicação com Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copia tudo do projeto para o container de build
COPY N.I.B/pom.xml .
COPY N.I.B/src ./src

# Executa o build (sem rodar os testes)
RUN mvn clean package -DskipTests

# Etapa 2: Container leve apenas com o JAR gerado
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/N.I.B-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
