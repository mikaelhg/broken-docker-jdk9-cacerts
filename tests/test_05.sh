#!/bin/bash

apt update && apt -qy install openjdk-1*-jdk-headless nano less

# JKS, password "changeit"
/usr/bin/printf '\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\xe2\x68\x6e\x45\xfb\x43\xdf\xa4\xd9\x92\xdd\x41\xce\xb6\xb2\x1c\x63\x30\xd7\x92' > /etc/ssl/certs/java/cacerts

/var/lib/dpkg/info/ca-certificates-java.postinst configure

java -cp . TestHttps "https://www.google.com/"
