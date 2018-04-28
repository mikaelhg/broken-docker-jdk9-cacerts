# Ubuntu 18.04, Debian - broken Java `cacerts` file

Ubuntu 18.04 and Debian Java 9, 10 and 11 `cacerts` file is now `pkcs12` format, and requires a password.

It used to be in the `jks` format, which worked fine with an empty password.

Java programs which need to use JDK's SSL functionality have to access the `cacerts`
file, and the only way to get the decryption password to the JVM is by using the
`-Djavax.net.ssl.trustStorePassword=changeit` command line parameter.

If you don't set that parameter when starting your JVM, it uses the default
empty password (""), which is how you get the error message:

    java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty

which must have placed high in the "least informative error message of 2018" competition.

---------------

## TL;DR Workaround

```bash
echo "storepass=''" >> /etc/default/cacerts
/usr/bin/printf '\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\x57\xbe\xbc\x27\x62\xa2\x1d\x70\xff\xf2\x18\xdd\x59\x68\x01\x1f\xfe\x42\x3a\x69' > /etc/ssl/certs/java/cacerts
/var/lib/dpkg/info/ca-certificates-java.postinst configure
```

---------------

## Links to workarounds and bug trackers

https://gist.github.com/mikaelhg/527204e746984cf9a33f7910bb8b4cb6

docker-library/openjdk/issues/145

[Ubuntu 1739631: Fresh install with JDK 9 can't use the generated PKCS12 cacerts keystore file](https://bugs.launchpad.net/ubuntu/+source/ca-certificates-java/+bug/1739631)

[Debian 894979: ca-certificates-java: does not work with OpenJDK 9, applications fail with InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty](https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=894979)

[JEP 229: Create PKCS12 Keystores by Default](http://openjdk.java.net/jeps/229)

[JDK-8044445 : JEP 229: Create PKCS12 Keystores by Default](https://bugs.java.com/view_bug.do?bug_id=8044445)

[Debian ca-certificates-java ChangeLog](http://metadata.ftp-master.debian.org/changelogs/main/c/ca-certificates-java/ca-certificates-java_20180413_changelog)

---------------

## Moar tools

See what your JDK's SSL stack is doing with

    -Djavax.net.debug=ssl:handshake

Generate empty passwordless JKS keystore files with

    pip3 install pyjks
    python3 generate_empty_jks_keystore.py

    javac --release 8 GenerateEmptyJKSKeystore.java
    java -cp . GenerateEmptyJKSKeystore

Attempt a SSL connection to see if your JVM can access the `cacerts` file

    java -cp . TestHttps 'https://www.google.com/'

    java -Djavax.net.ssl.trustStorePassword=changeit -cp . TestHttps "https://www.google.com/"

Build an Ubuntu 18.04 Docker image with this workaround:

    docker build --pull -t bjc .

Test that image. A stack trace is a failure, an empty line is a success:

    docker run -it --rm bjc

Test various solutions on different platforms:

    docker run -it --rm -v `pwd`:/app:ro -w /app ubuntu:18.04 bash tests/test_01.sh
    docker run -it --rm -v `pwd`:/app:ro -w /app ubuntu:18.04 bash tests/test_02.sh
    docker run -it --rm -v `pwd`:/app:ro -w /app ubuntu:18.04 bash tests/test_03.sh
    docker run -it --rm -v `pwd`:/app:ro -w /app ubuntu:18.04 bash tests/test_04.sh

    docker run -it --rm -v `pwd`:/app:ro -w /app debian:testing bash tests/test_01.sh
    docker run -it --rm -v `pwd`:/app:ro -w /app debian:testing bash tests/test_02.sh
    docker run -it --rm -v `pwd`:/app:ro -w /app debian:testing bash tests/test_03.sh
    docker run -it --rm -v `pwd`:/app:ro -w /app debian:testing bash tests/test_04.sh

## Exception

```text
javax.net.ssl.SSLException: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty
	at java.base/sun.security.ssl.Alerts.getSSLException(Alerts.java:214)
	at java.base/sun.security.ssl.SSLSocketImpl.fatal(SSLSocketImpl.java:1974)
	at java.base/sun.security.ssl.SSLSocketImpl.fatal(SSLSocketImpl.java:1926)
	at java.base/sun.security.ssl.SSLSocketImpl.handleException(SSLSocketImpl.java:1909)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1436)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1413)
	at java.base/sun.net.www.protocol.https.HttpsClient.afterConnect(HttpsClient.java:567)
	at java.base/sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.connect(AbstractDelegateHttpsURLConnection.java:185)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1581)
	at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1509)
	at java.base/java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:527)
	at java.base/sun.net.www.protocol.https.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:329)
	at TestHttps.main(TestHttps.java:8)
Caused by: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty
	at java.base/sun.security.validator.PKIXValidator.<init>(PKIXValidator.java:89)
	at java.base/sun.security.validator.Validator.getInstance(Validator.java:181)
	at java.base/sun.security.ssl.X509TrustManagerImpl.getValidator(X509TrustManagerImpl.java:330)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkTrustedInit(X509TrustManagerImpl.java:180)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkTrusted(X509TrustManagerImpl.java:192)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:133)
	at java.base/sun.security.ssl.ClientHandshaker.checkServerCerts(ClientHandshaker.java:1947)
	at java.base/sun.security.ssl.ClientHandshaker.serverCertificate(ClientHandshaker.java:1777)
	at java.base/sun.security.ssl.ClientHandshaker.processMessage(ClientHandshaker.java:264)
	at java.base/sun.security.ssl.Handshaker.processLoop(Handshaker.java:1098)
	at java.base/sun.security.ssl.Handshaker.processRecord(Handshaker.java:1026)
	at java.base/sun.security.ssl.SSLSocketImpl.processInputRecord(SSLSocketImpl.java:1137)
	at java.base/sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:1074)
	at java.base/sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:973)
	at java.base/sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1402)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1429)
	... 8 more
Caused by: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty
	at java.base/java.security.cert.PKIXParameters.setTrustAnchors(PKIXParameters.java:200)
	at java.base/java.security.cert.PKIXParameters.<init>(PKIXParameters.java:120)
	at java.base/java.security.cert.PKIXBuilderParameters.<init>(PKIXBuilderParameters.java:104)
	at java.base/sun.security.validator.PKIXValidator.<init>(PKIXValidator.java:86)
	... 23 more
```