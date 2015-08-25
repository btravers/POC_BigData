#!/bin/bash

N=3

docker rm -f master &> /dev/null
echo "start master container..."
docker run -d -t --dns 127.0.0.1 -P --name master -h master.kiwenlau.com -v "$PWD"/ml-1m:/data -w /root btravers/spark:0.1.0 &> /dev/null

FIRST_IP=$(docker inspect --format="{{.NetworkSettings.IPAddress}}" master)

i=1
while [ $i -lt $N ]
do
	docker rm -f slave$i &> /dev/null
	echo "start slave$i container..."
	docker run -d -t --dns 127.0.0.1 -P --name slave$i -h slave$i.kiwenlau.com -e JOIN_IP=$FIRST_IP kiwenlau/hadoop-slave:0.1.0 &> /dev/null
	((i++))
done

#docker exec master /root/start-hadoop.sh
docker exec -it master bash