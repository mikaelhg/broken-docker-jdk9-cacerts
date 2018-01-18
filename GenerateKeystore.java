/*
  KeyStore check by @mikaelhg
  javac --release 8 GenerateKeystore.java
 */

import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.util.*;

class GenerateKeystore {

    private static void storeAndPrint(final KeyStore keyStore, final String password) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (null == password) {
                keyStore.store(baos, null);
            } else {
                keyStore.store(baos, password.toCharArray());
            }
            final byte firstByte = baos.toByteArray()[0];
            System.out.printf("%15s - %02x\n", password, firstByte);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void testKeyStore(final KeyStore keyStore, final String[] passwords) throws Exception {
        keyStore.load(null, null);
        for (final String pwd : passwords) {
            storeAndPrint(keyStore, pwd);
        }
    }

    public static void main(String[] args) throws Exception {
        final String[] types = {"JKS", "PKCS12", KeyStore.getDefaultType()};
        final String[] passwords = {"randompassword", "changeit", ""};
        for (final String storeType : types) {
            System.out.printf("\n%s\n", storeType);
            final KeyStore keyStore = KeyStore.getInstance(storeType);
            testKeyStore(keyStore, passwords);
        }
    }

}
