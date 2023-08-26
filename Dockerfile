FROM openjdk:11-jre-slim
WORKDIR /app

COPY build/libs/service1-1.0-SNAPSHOT-microbundle.jar /app/service1-1.0-SNAPSHOT-microbundle.jar


CMD ["java", "-jar", "service1-1.0-SNAPSHOT-microbundle.jar"]