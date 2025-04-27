FROM openjdk:17
ARG JAR_FILE=target/RMKarts.jar
COPY ${JAR_FILE} RMKarts.jar
ENTRYPOINT ["java","-jar","/RMKarts.jar"]