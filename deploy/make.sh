#!/bin/bash

cd ..

if [ -f sales-manager-1.0-SNAPSHOT.war ]; then
    rm sales-manager-1.0-SNAPSHOT.war
fi

mvn clean install

cp ./target/sales-manager-1.0-SNAPSHOT.war ./deploy/

cd deploy

docker build -t sales-manager .

docker run -it sales-manager
