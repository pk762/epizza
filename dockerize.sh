#!/usr/bin/env bash

set -e

if echo $@ | grep -q 'infra'; then
echo
echo "#############################################"
echo "# dockerizing 'config-server'               #"
echo "#############################################"
./config-server/gradlew -p config-server buildDockerImage -x check
fi

if echo $@ | grep -q 'services'; then
echo
echo "#############################################"
echo "# dockerizing 'order'                       #"
echo "#############################################"
./order/gradlew -p order buildDockerImage -x check

echo
echo "#############################################"
echo "# dockerizing 'bakery'                      #"
echo "#############################################"
./bakery/gradlew -p bakery buildDockerImage -x check

echo
echo "#############################################"
echo "# dockerizing 'delivery'                    #"
echo "#############################################"
./delivery/gradlew -p delivery buildDockerImage -x check
fi

if echo $@ | grep -q 'ui'; then
echo
echo "#############################################"
echo "# dockerizing 'order-ui'                    #"
echo "#############################################"
docker build --no-cache -t epizza/order-ui:latest order-ui
fi
