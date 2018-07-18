#!/bin/bash

apt -qq update && apt -qqy dist-upgrade

apt -qq update && apt -qqy install openjdk-1*-jdk-headless nano less

echo "deb http://archive.ubuntu.com/ubuntu/ bionic-proposed restricted main multiverse universe" > /etc/apt/sources.list.d/bionic-proposed.list

apt update && apt -qqy install ca-certificates-java

java -cp . TestHttps "https://www.google.com/"
