FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/mpd-search-engine.jar mpd-search-engine.jar
ENTRYPOINT ["java", "-jar", "/mpd-search-engine.jar"]
EXPOSE 9090
