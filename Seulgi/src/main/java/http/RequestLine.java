package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);

    private String method;
    private String path;
    private String ext;
    private Map<String, String> params = new HashMap<>();

    public RequestLine(String line) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        path = tokens[1];
        ext = "";

        String[] url = path.split("[.]");
        if (url.length > 1) {
            ext = url[url.length-1];
        }
    }

    public void setBodyParams(BufferedReader br) throws IOException {
        String body = null;
        int contentLength = 0;
        String read = null;
        while ((read = br.readLine()) != null) {
            if (read != null && read.contains("Content-Length")) {
                contentLength = Integer.parseInt(IOUtils.bodyData(read)[1]);
            }

            if (read.length() == 0) {
                body = IOUtils.readData(br, contentLength);
                params = HttpRequestUtils.parseQueryString(body);
                break;
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getExt() { return ext; }

    public Map<String, String> getParams() {
        return params;
    }
}
