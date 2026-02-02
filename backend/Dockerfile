# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1) Cache deps (optimise les rebuilds)
# On ne copie que le pom d'abord
COPY skillforge/pom.xml ./pom.xml
RUN mvn -e -DskipTests dependency:go-offline

# 2) Puis on copie tout le projet Maven (sources + resources + éventuels sous-dossiers)
COPY skillforge/ ./

# 3) Build
RUN mvn -e -DskipTests clean package


# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# utilisateur non-root
RUN useradd -m spring
USER spring

# copie du jar produit
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
