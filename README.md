The Docker image openjdk:9-jdk has a broken cacerts file.

---------------

# TL;DR Workaround

Used `pyjks` to create a minimal JKS file with an empty password, since JDK's `keytool` doesn't do that anymore. From there on, we'll rely on the certificate compatibility mode.

```bash
echo "storepass=''" >> /etc/default/cacerts
echo -e "\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\x57\xbe\xbc\x27\x62\xa2\x1d\x70\xff\xf2\x18\xdd\x59\x68\x01\x1f\xfe\x42\x3a\x69" > /etc/ssl/certs/java/cacerts
/var/lib/dpkg/info/ca-certificates-java.postinst configure
```

---------------

```text
java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty
```

https://github.com/docker-library/openjdk/issues/145

https://github.com/docker-library/openjdk/issues/19

https://packages.debian.org/source/sid/ca-certificates-java

http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/67a15c76095d/src/share/classes/sun/security/provider/JavaKeyStore.java

java -Djavax.net.ssl.trustStore=custompath/cacerts -Djavax.net.ssl.trustStorePassword=changeit

-Djavax.net.debug=ssl:handshake

```bash
java -cp . TestHttps 'https://www.google.com/'

java -Djavax.net.ssl.trustStorePassword=changeit -cp . TestHttps "https://www.google.com/"
```

-Djavax.net.ssl.trustStoreType=jks

```bash

apt update && apt -y install nano less file

ls -Flah /etc/ssl/certs/java/cacerts
file /etc/ssl/certs/java/cacerts

rm /etc/ssl/certs/java/cacerts
/var/lib/dpkg/info/ca-certificates-java.postinst configure


docker run -it --rm openjdk:9-jdk find /usr/lib/jvm/java-9-openjdk-amd64 -type f -print0 | xargs -0 md5sum > sums.9-jdk
docker run -it --rm openjdk:9-jdk-slim find /usr/lib/jvm/java-9-openjdk-amd64 -type f -print0 | xargs -0 md5sum > sums.9-jdk-slim

```
