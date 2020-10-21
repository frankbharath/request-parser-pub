FROM openjdk:11
EXPOSE 8080
ADD target/rentpal-live.jar rentpal-live.jar
ENTRYPOINT ["java", "-jar", "/rentpal-live.jar"]