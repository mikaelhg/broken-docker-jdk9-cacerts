#!/bin/bash

apt update && apt -y install openjdk-1*-jdk-headless nano less

update-ca-certificates -f

/var/lib/dpkg/info/ca-certificates-java.postinst configure

java -cp . TestHttps "https://www.google.com/"
