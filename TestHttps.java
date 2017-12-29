/**
 * Issue https://github.com/docker-library/openjdk/issues/145 reproduction by @keeganwitt.
 */

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class TestHttps {
    public static void main(String[] args) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(args[0]).openConnection();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
