# ─────────── Stage 1: build com Java 19 + Maven ───────────
FROM eclipse-temurin:19-jdk AS build

# instala o Maven
RUN apt-get update \
 && apt-get install -y maven \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# cache das dependências
#antes de copiar o pom precisa clonar o projeto.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# compila e gera o WAR
COPY src ./src
RUN mvn clean package -DskipTests -B


# ─────────── Stage 2: WildFly sobre Java 19 ───────────
FROM eclipse-temurin:19-jdk AS runtime

ENV WILDFLY_VERSION=36.0.0.Final \
    WILDFLY_HOME=/opt/wildfly

# baixa e instala o WildFly
RUN apt-get update \
 && apt-get install -y curl tar gzip --no-install-recommends \
 && rm -rf /var/lib/apt/lists/* \
 && curl -SL https://github.com/wildfly/wildfly/releases/download/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.tar.gz \
      | tar -xz -C /opt \
 && mv /opt/wildfly-${WILDFLY_VERSION} ${WILDFLY_HOME} \
 && chmod +x ${WILDFLY_HOME}/bin/*.sh

# deploy do WAR gerado
COPY --from=build /app/target/clinica.war \
     ${WILDFLY_HOME}/standalone/deployments/

EXPOSE 8080

CMD ["bash","-lc","${WILDFLY_HOME}/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0"]
