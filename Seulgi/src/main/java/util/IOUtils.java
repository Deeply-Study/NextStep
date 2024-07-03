package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
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

    public static String extData(String url) {
        String[] arr = url.split("[.]");
        if (arr.length > 1) {
            return arr[arr.length-1];
        } else {
            return "";
        }
    }

    /**
     * key value -> value값 반환
     * @param line
     * @return
     */
    public static String[] bodyData(String line) {
        String[] data = line.replaceAll(" ", "").split(":");
        return data;
    }
}
