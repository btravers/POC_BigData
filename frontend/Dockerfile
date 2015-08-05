FROM ubuntu:15.10

MAINTAINER TRAVERS Benoit <benoit.travers@zenika.com>

RUN apt-get update

RUN apt-get install apache2 -y

COPY dist /app
RUN mkdir -p /app && rm -rf /var/www/html && ln -s /app /var/www/html

RUN a2enmod proxy_http

RUN a2dissite 000-default.conf
RUN rm /etc/apache2/sites-available/000-default.conf
COPY movie_library.conf /etc/apache2/sites-available/000-default.conf
RUN a2ensite 000-default.conf

RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf

COPY run.sh /run.sh

RUN chmod +x /run.sh

EXPOSE 80

CMD ["/run.sh"]
