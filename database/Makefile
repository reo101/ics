#!/usr/bin/make -f

fresh: clean run

clean:
	sudo docker-compose down

.DEFAULT_GOAL := run
run:
	sudo docker-compose up

.PHONY: clean fresh run
