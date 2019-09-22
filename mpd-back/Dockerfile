FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/mpd-back.jar mpd-back.jar
ENTRYPOINT ["java", "-jar", "/mpd-back.jar"]
EXPOSE 8080
