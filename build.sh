#!/usr/bin/env bash

set -e

COMPONENT=${@:-all}

RED='\033[0;31m'
NC='\033[0m' # No Color

if echo ${COMPONENT} | grep -q 'all\|infra\|plugin\|gradle'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'gradle-plugins'                 |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./gradle-plugins/gradlew -p gradle-plugins build pTML
fi

if echo ${COMPONENT} | grep -q 'all\|infra\|starter\|messag'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'messaging-boot-starter'         |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./messaging-boot-starter/gradlew -p messaging-boot-starter build pTML
fi

if echo ${COMPONENT} | grep -q 'all\|infra\|config'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'config-server'                  |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./config-server/gradlew -p config-server build
fi

if echo ${COMPONENT} | grep -q 'all\|service\|order'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'order'                          |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./order/gradlew -p order build pTML
fi

if echo ${COMPONENT} | grep -q 'all\|service\|bakery'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'bakery'                         |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./bakery/gradlew -p bakery build
fi

if echo ${COMPONENT} | grep -q 'all\|ui\|delivery'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'delivery'                       |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./delivery/gradlew -p delivery build
fi

if echo ${COMPONENT} | grep -q 'all\|ui'; then
echo
echo -e "${RED}+-------------------------------------------+${NC}"
echo -e "${RED}| building 'order-ui'                       |${NC}"
echo -e "${RED}+-------------------------------------------+${NC}"
./order-ui/gradlew -p order-ui build
fi
