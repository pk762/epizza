#!/usr/bin/env bash

set -e

./gradle-plugins/gradlew -p gradle-plugins build pTML
./messaging-boot-starter/gradlew -p messaging-boot-starter build pTML
./config-server/gradlew -p config-server clean
./order/gradlew -p order clean
./bakery/gradlew -p bakery clean
./delivery/gradlew -p delivery clean
./order-ui/gradlew -p order-ui clean
