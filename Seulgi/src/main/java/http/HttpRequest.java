package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    private RequestLine requestLine;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in, "euc-kr"));

        String line = bf.readLine();
        if (!"".equals(line) && line != null) {
            requestLine = new RequestLine(line);
            //log.debug("line : {} {}", requestLine.getMethod(), requestLine.getPath());

            if ("POST".equals(requestLine.getMethod())) {
                requestLine.setBodyParams(bf);
                params = requestLine.getParams();
            } else {
                setHeaders(bf, "Cookie");
            }
        }
    }

    public void setHeaders(BufferedReader br, String header) throws IOException {
        String response = null;
        String read = null;
        while ((read = br.readLine()) != null) {
            if (read == null || "".equals(read) || " ".equals(read)) break;
            if (read != null && read.contains(header)) {
                response = IOUtils.bodyData(read)[1];
                headers = HttpRequestUtils.parseCookies(response);
                break;
            }
        }
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }
}
