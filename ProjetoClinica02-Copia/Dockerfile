# Stage 1: Build the WAR using Maven
FROM maven:3.8.6-jdk-11-slim AS builder
WORKDIR /app

# Copy POM and patch compiler plugin to use Java 11
COPY pom.xml .
RUN sed -i 's/<source>14<\/source>/<source>11<\/source>/' pom.xml \
    && sed -i 's/<target>14<\/target>/<target>11<\/target>/' pom.xml

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# Stage 2: Deploy WAR on Tomcat
FROM tomcat:9-jdk11-openjdk-slim

# Maintainer and encoding
LABEL maintainer="Tiago Tonon <tiago@example.com>"
ENV JAVA_OPTS="-Dfile.encoding=UTF-8"

# Deploy WAR as ROOT
COPY --from=builder /app/target/clinica.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
