#!/usr/bin/env bash

set -e


if echo $@ | grep -q 'all\|infra\|plugin\|gradle'; then
echo
echo "#############################################"
echo "# building 'gradle-plugins'                 #"
echo "#############################################"
./gradle-plugins/gradlew -p gradle-plugins build pTML
fi

if echo $@ | grep -q 'all\|infra\|starter\|messag'; then
echo
echo "#############################################"
echo "# building 'messaging-boot-starter'         #"
echo "#############################################"
./messaging-boot-starter/gradlew -p messaging-boot-starter build pTML
fi

if echo $@ | grep -q 'all\|infra\|config'; then
echo
echo "#############################################"
echo "# building 'config-server'                  #"
echo "#############################################"
./config-server/gradlew -p config-server build
fi

if echo $@ | grep -q 'all\|service\|order'; then
echo
echo "#############################################"
echo "# building 'order'                          #"
echo "#############################################"
./order/gradlew -p order build pTML
fi

if echo $@ | grep -q 'all\|service\|bakery'; then
echo
echo "#############################################"
echo "# building 'bakery'                         #"
echo "#############################################"
./bakery/gradlew -p bakery build
fi

if echo $@ | grep -q 'all\|service\|delivery'; then
echo
echo "#############################################"
echo "# building 'delivery'                       #"
echo "#############################################"
./delivery/gradlew -p delivery build
fi

if echo $@ | grep -q 'all\|ui'; then
echo
echo "#############################################"
echo "# building 'order-ui'                       #"
echo "#############################################"
./order-ui/gradlew -p order-ui build
fi
