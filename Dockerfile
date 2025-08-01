FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} restaurant-ordering-webapp.jar

ENTRYPOINT ["java","-jar","restaurant-ordering-webapp.jar"]

EXPOSE 8080

# Set Spring profile to 'docker'
ENV SPRING_PROFILES_ACTIVE=docker