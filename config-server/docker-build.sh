#!/usr/bin/env bash

NAMESPACE=epizza
NAME=config-server
TAG=latest
IMAGE=${NAMESPACE}/${NAME}:${TAG}

docker build --no-cache -t ${IMAGE} .
#docker push ${IMAGE}
