#!/bin/bash

set -e

dockerComposeUp="docker-compose -f docker-compose-mysql-binlog.yml up -d"
dockerComposeDown="docker-compose -f docker-compose-mysql-binlog.yml down"

./gradlew testClasses

${dockerComposeDown}

./gradlew -x :end-to-end-tests:test build

${dockerComposeUp}

./wait-for-mysql.sh
./wait-for-services.sh localhost actuator/health 8081 8082

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

${dockerComposeDown}