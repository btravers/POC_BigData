NAME := btravers/movie_library
VERSION := $(or $(VERSION),$(VERSION),'1.0.0')

all:
	clean 
	build

build:
	npm install
	bower install
	gulp build
	docker build $(DOCKER_OPTS) -t $(NAME):$(VERSION) .

clean:
	rm -rf bower_components
	rm -rf node_modules
	rm -rf dist

tag_latest:
	docker tag $(NAME):$(VERSION) $(NAME):latest

