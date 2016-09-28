#!/usr/bin/env bash

set -e

COMPONENT=${@:-all}

RED='\033[0;31m'
NC='\033[0m' # No Color

if echo ${COMPONENT} | grep -q 'all\|infra\|config'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| dockerizing 'config-server'               |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./config-server/gradlew -p config-server buildDockerImage -x check
fi

if echo ${COMPONENT} | grep -q 'all\|service\|order'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| dockerizing 'order'                       |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./order/gradlew -p order buildDockerImage -x check
fi

if echo ${COMPONENT} | grep -q 'all\|service\|bakery'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| dockerizing 'bakery'                      |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./bakery/gradlew -p bakery buildDockerImage -x check
fi

if echo ${COMPONENT} | grep -q 'all\|ui\|delivery'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| dockerizing 'delivery'                    |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./delivery/gradlew -p delivery buildDockerImage -x check
fi

if echo ${COMPONENT} | grep -q 'all\|ui'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| dockerizing 'order-ui'                    |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
docker build --no-cache -t epizza/order-ui:latest order-ui
fi
