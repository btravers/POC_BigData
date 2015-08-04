NAME := btravers/movie_library_api
VERSION := $(or $(VERSION),$(VERSION),'1.0.0')

all:
	clean 
	build

build:
	mvn package
	docker build $(DOCKER_OPTS) -t $(NAME):$(VERSION) .

clean:
	mvn clean

tag_latest:
	docker tag $(NAME):$(VERSION) $(NAME):latest

