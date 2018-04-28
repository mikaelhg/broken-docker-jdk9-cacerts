import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.util.*;

class GenerateEmptyJKSKeystore {

    public static void main(final String[] args) throws Exception {
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        keyStore.store(baos, "".toCharArray());
        final StringBuilder sb = new StringBuilder();
        for (byte b : baos.toByteArray()) {
            sb.append(String.format("\\x%02x", b));
        }
        System.out.println(sb.toString());
    }

}
