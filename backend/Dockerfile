FROM tomcat:8.0

MAINTAINER TRAVERS Benoit <benoit.travers@zenika.com>

COPY target/*.war $CATALINA_HOME/webapps/

RUN mv $CATALINA_HOME/webapps/*.war $CATALINA_HOME/webapps/backend.war

ENTRYPOINT ["catalina.sh", "run"]

ENV CATALINA_OPTS="-Des.host=es.url"