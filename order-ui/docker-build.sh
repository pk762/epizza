#!/usr/bin/env bash

NAMESPACE=epizza
NAME=order-ui
TAG=latest
IMAGE=${NAMESPACE}/${NAME}:${TAG}

docker build --no-cache -t ${IMAGE} .
#docker push ${IMAGE}
