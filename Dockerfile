# --- Estágio 1: Construção (Build) ---
# Usamos uma imagem que já tem Maven e Java 21 instalados
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Definimos a pasta de trabalho
WORKDIR /app

# Copiamos todo o projeto para dentro do container
COPY . .

# Rodamos o comando para gerar o .jar (pulando testes para ser mais rápido)
RUN mvn clean package -DskipTests

# --- Estágio 2: Execução (Run) ---
# Usamos uma imagem leve apenas com o Java (sem Maven) para rodar o site
FROM eclipse-temurin:21-jre-alpine

# Definimos a pasta de trabalho
WORKDIR /app

# Copiamos o .jar gerado no estágio anterior para cá
COPY --from=build /app/target/*.jar app.jar

# Expomos a porta (apenas documentação, o Railway controla isso via variável)
EXPOSE 8080

# Alteração para suportar a injeção de porta do Render
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]