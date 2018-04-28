#!/bin/bash

apt update && apt -y install openjdk-1*-jdk-headless nano less

echo "storepass=''" >> /etc/default/cacerts

# JKS, generated with Python
/usr/bin/printf '\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\x57\xbe\xbc\x27\x62\xa2\x1d\x70\xff\xf2\x18\xdd\x59\x68\x01\x1f\xfe\x42\x3a\x69' > /etc/ssl/certs/java/cacerts

/var/lib/dpkg/info/ca-certificates-java.postinst configure

java -cp . TestHttps "https://www.google.com/"
