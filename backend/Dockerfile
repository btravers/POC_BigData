FROM tomcat:8.0

MAINTAINER TRAVERS Benoit <benoit.travers@zenika.com>

RUN rm -rf $CATALINA_HOME/webapps/ROOT

COPY target/*.war $CATALINA_HOME/webapps/

RUN mv $CATALINA_HOME/webapps/*.war $CATALINA_HOME/webapps/ROOT.war

ENTRYPOINT ["catalina.sh", "run"]

ENV CATALINA_OPTS="-Des.host=es.url"