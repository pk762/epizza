#!/usr/bin/env bash

set -e

echo
echo "#############################################"
echo "# building 'gradle-plugins'                 #"
echo "#############################################"
./gradle-plugins/gradlew -p gradle-plugins build pTML

echo
echo "#############################################"
echo "# building 'shared'                         #"
echo "#############################################"
./shared/gradlew -p shared build pTML

echo
echo "#############################################"
echo "# building 'config-server'                  #"
echo "#############################################"
./shared/gradlew -p config-server check bootRepackage

echo
echo "#############################################"
echo "# building 'order'                          #"
echo "#############################################"
./order/gradlew -p order check bootRepackage
#docker-compose build --no-cache order

echo
echo "#############################################"
echo "# building 'bakery'                         #"
echo "#############################################"
./bakery/gradlew -p bakery check bootRepackage
#docker-compose build --no-cache bakery

echo
echo "#############################################"
echo "# building 'delivery'                       #"
echo "#############################################"
./delivery/gradlew -p delivery check bootRepackage
#docker-compose build --no-cache delivery

echo
echo "#############################################"
echo "# building 'order-ui'                       #"
echo "#############################################"
./order-ui/gradlew -p order-ui check bootRepackage
#docker-compose build --no-cache orderui
