FROM kiwenlau/hadoop-master:0.1.0

RUN curl -s http://d3kbcqa49mib13.cloudfront.net/spark-1.4.1-bin-hadoop2.3.tgz | tar -xz -C /usr/local/
RUN ln -s /usr/local/spark-1.4.1-bin-hadoop2.3 /usr/local/spark
ENV SPARK_HOME /usr/local/spark

COPY target/*-jar-with-dependencies.jar /

RUN echo "#!/bin/bash" > $SPARK_HOME/conf/spark-env.sh
RUN echo 'export SPARK_DIST_CLASSPATH=$(hadoop classpath)' >> $SPARK_HOME/conf/spark-env.sh
RUN chmod +x $SPARK_HOME/conf/spark-env.sh

CMD '/root/start-ssh-serf.sh'; 'bash'