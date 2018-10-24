#!/usr/bin/env bash

cd ../front-server
./gradlew shadowJar
cp build/libs/front-server.jar ../docker/front-server/front-server.jar

cd ..

cp -r ml-server/src docker/ml-server/src