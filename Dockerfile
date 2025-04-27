# Usa imagem do Maven para construir o projeto
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o pom.xml e o src da pasta N.I.B
COPY N.I.B/pom.xml .
COPY N.I.B/src ./src

# Faz o build do projeto
RUN mvn clean package -DskipTests

# Usa imagem do OpenJDK para rodar o projeto
FROM openjdk:17-jdk

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o jar gerado do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Comando para rodar o projeto
ENTRYPOINT ["java", "-jar", "app.jar"]
