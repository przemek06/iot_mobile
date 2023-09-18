#Stage 1
FROM maven:3.9.4-eclipse-temurin-17 as stage1
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
WORKDIR /build/
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true

#Stage 2
FROM eclipse-temurin:17-jdk
WORKDIR /app/
COPY --from=stage1 /build/target/iotmobile-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "iotmobile-0.0.1-SNAPSHOT.jar"]