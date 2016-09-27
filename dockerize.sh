#!/usr/bin/env bash

set -e

if echo $@ | grep -q 'all\|infra\|config'; then
echo
echo "#############################################"
echo "# dockerizing 'config-server'               #"
echo "#############################################"
./config-server/gradlew -p config-server buildDockerImage -x check
fi

if echo $@ | grep -q 'all\|service\|order'; then
echo
echo "#############################################"
echo "# dockerizing 'order'                       #"
echo "#############################################"
./order/gradlew -p order buildDockerImage -x check
fi

if echo $@ | grep -q 'all\|service\|bakery'; then
echo
echo "#############################################"
echo "# dockerizing 'bakery'                      #"
echo "#############################################"
./bakery/gradlew -p bakery buildDockerImage -x check
fi

if echo $@ | grep -q 'all\|service\|delivery'; then
echo
echo "#############################################"
echo "# dockerizing 'delivery'                    #"
echo "#############################################"
./delivery/gradlew -p delivery buildDockerImage -x check
fi

if echo $@ | grep -q 'all\|ui'; then
echo
echo "#############################################"
echo "# dockerizing 'order-ui'                    #"
echo "#############################################"
docker build --no-cache -t epizza/order-ui:latest order-ui
fi
