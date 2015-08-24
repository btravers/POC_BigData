NAME := btravers/spark
VERSION := '0.1.0'

all: clean build

build:
	mvn package
	docker build $(DOCKER_OPTS) -t $(NAME):$(VERSION) .

clean:
	mvn clean

