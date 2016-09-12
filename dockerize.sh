#!/usr/bin/env bash

set -e

echo
echo "#############################################"
echo "# dockerizing 'config-server'               #"
echo "#############################################"
docker build --no-cache -t epizza/config-server:latest config-server

echo
echo "#############################################"
echo "# dockerizing 'order'                       #"
echo "#############################################"
./order/gradlew -p order buildDockerImage

echo
echo "#############################################"
echo "# dockerizing 'bakery'                      #"
echo "#############################################"
./bakery/gradlew -p bakery buildDockerImage

echo
echo "#############################################"
echo "# dockerizing 'delivery'                    #"
echo "#############################################"
./delivery/gradlew -p delivery buildDockerImage

echo
echo "#############################################"
echo "# dockerizing 'order-ui'                    #"
echo "#############################################"
docker build --no-cache -t epizza/order-ui:latest order-ui
