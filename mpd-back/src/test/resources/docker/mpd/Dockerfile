FROM ubuntu

MAINTAINER Aleksandr N Potashev <anpotashev@hotmail.com>

EXPOSE 6600
EXPOSE 8000

VOLUME ["/opt/mpd/mpd"]
VOLUME ["/opt/mpd/music"]
VOLUME ["/opt/mpd/playlists"]

RUN apt-get update \
&& apt-get install mpd -y \
&& rm -rf /var/lib/apt/lists/*

ADD ./mpd.conf /etc/

RUN mkdir - /opt/mpd/run \
&& chown mpd:audio /opt/mpd/run

ENTRYPOINT mpd --no-daemon --stderr /etc/mpd.conf
