FROM openjdk:11-jdk as BUILDER
WORKDIR /build
COPY mvnw /build/
COPY pom.xml /build/
COPY .mvn /build/.mvn
COPY src /build/src
RUN ./mvnw package -DskipTests

FROM openjdk:11-jre-slim
ARG VERSION=0.0.1-SNAPSHOT
ARG APP_NAME=PeopleMeals
#ADD src/main/resources/application-dev.yml /app/application.yml
WORKDIR /app
COPY --from=BUILDER /build/target/${APP_NAME}-${VERSION}.jar /app/myapp.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dedockerv/./urandom","-jar","/app/myapp.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dedockerv/./urandom --spring.config.location=classpath:file:/app/application.yml","-jar","/app/myapp.jar"]