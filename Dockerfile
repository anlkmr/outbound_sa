# Stage 1: Build Stage
FROM eclipse-temurin:17-jdk-jammy AS build
#FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
#RUN rm -rf /app/*
# Clone the Spring Boot application from Git
#RUN apt-get update && apt-get install -y git
#RUN apt-get update -y && apt-get install git -y && apt-get install maven -y && apt-get install nodejs -y && apt-get install npm && npm install -g copyfiles@latest -y && apt-get install npmcli -y
RUN apt-get update -y && \
    apt-get install -y git && \
    apt-get install -y maven \

RUN git clone -b dev-feature-main https://anilkadiga1:ATBBY6Ew583aW6gY7seMnDBZRJxf96971FCC@bitbucket.org/cestasoft/bpe-mobileservices-outbound.git .
#WORKDIR /app/bpe-mobileservices-outbound
# Build the Spring Boot application

RUN ls
RUN mvn package -DskipTests

#RUN ./mvnw dependency:resolve

# Stage 2: Package Stage
FROM openjdk:17.0.2-slim
WORKDIR /app
# Copy the JAR file from the build stage to the current location
COPY --from=build /app/target/OutboundProcessor-v1.jar .
# Expose the port your Spring Boot app will run on
EXPOSE 8145
# Command to run the Spring Boot application
CMD ["java", "-jar", "OutboundProcessor-v1.jar"]
