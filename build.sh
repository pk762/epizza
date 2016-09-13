#!/usr/bin/env bash

set -e

echo
echo "#############################################"
echo "# building 'gradle-plugins'                 #"
echo "#############################################"
./gradle-plugins/gradlew -p gradle-plugins build pTML

echo
echo "#############################################"
echo "# building 'messaging-boot-starter'         #"
echo "#############################################"
./messaging-boot-starter/gradlew -p messaging-boot-starter build pTML

echo
echo "#############################################"
echo "# building 'config-server'                  #"
echo "#############################################"
./config-server/gradlew -p config-server build

echo
echo "#############################################"
echo "# building 'order'                          #"
echo "#############################################"
./order/gradlew -p order build

echo
echo "#############################################"
echo "# building 'bakery'                         #"
echo "#############################################"
./bakery/gradlew -p bakery build

echo
echo "#############################################"
echo "# building 'delivery'                       #"
echo "#############################################"
./delivery/gradlew -p delivery build

echo
echo "#############################################"
echo "# building 'order-ui'                       #"
echo "#############################################"
./order-ui/gradlew -p order-ui build
