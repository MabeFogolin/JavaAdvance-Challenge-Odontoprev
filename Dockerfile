# Etapa 1: Build da aplicação com Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas arquivos necessários para evitar cache busting
COPY N.I.B/pom.xml .
COPY src ./src

# Executa o build do projeto (sem testes)
RUN mvn clean package -DskipTests

# Etapa 2: Container leve apenas com o JAR gerado
FROM eclipse-temurin:21-jdk-alpine

# Define o diretório padrão do app no container
WORKDIR /app

# Copia apenas o JAR gerado da etapa anterior
COPY --from=build /app/target/N.I.B-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta usada pelo Spring Boot
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
