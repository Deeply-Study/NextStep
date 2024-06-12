package util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    /**
     * URL 반환
     * @param line
     * @return
     */
    public static String urlData(String line) {
        String[] url = line.split(" ");
        return url[1];
    }

    /**
     * key value -> value값 반환
     * @param line
     * @return
     */
    public static String bodyData(String line) {
        String[] l = line.replaceAll(" ", "").split(":");
        return l[1];
    }
}
