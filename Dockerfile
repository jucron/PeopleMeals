FROM maven as BUILDER
WORKDIR /build
COPY pom.xml /build/
COPY src /build/src
COPY mysql /build/mysql
COPY .mvn /build/.mvn
RUN mvn install -DskipTests

FROM openjdk:11
ARG VERSION=0.0.1-SNAPSHOT
ARG APP_NAME=PeopleMeals
WORKDIR /app
COPY --from=BUILDER /build/target/${APP_NAME}-${VERSION}.jar /app/myapp.jar
CMD java -jar /app/myapp.jar