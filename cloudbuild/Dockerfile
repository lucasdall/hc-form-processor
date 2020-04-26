#
# Build stage
#
FROM maven:3.6.3-openjdk-14-slim AS build
WORKDIR /home/app/src
COPY . . 
VOLUME /tmp:/root/.m2
RUN mvn clean package

#
# Package stage
#
FROM openjdk:11.0.7-jre-slim
COPY --from=build /home/app/src/hc-form-processor-app/target/hc-form-processor-app-1.0.0.jar /usr/local/lib/hc-form-processor-app-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=hom", "-jar","/usr/local/lib/hc-form-processor-app-1.0.0.jar"]