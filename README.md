The Docker image openjdk:9-jdk has a broken cacerts file.

```text
java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty
```

https://github.com/docker-library/openjdk/issues/145

java -Djavax.net.ssl.trustStore=custompath/cacerts -Djavax.net.ssl.trustStorePassword=changeit

-Djavax.net.debug=ssl:handshake


