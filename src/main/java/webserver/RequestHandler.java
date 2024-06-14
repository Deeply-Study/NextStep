package webserver;

import static util.Constants.CONTENTE_LENGTH;
import static util.Constants.HTTP_HEADER_FIELD_SEPARATOR;
import static util.Constants.QUERY_STRING_PREFIX;
import static util.Constants.WEB_ROOT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private model.User user = new User();
    private IOUtils ioUtils = new IOUtils();
    private HttpRequestUtils httpRequestUtils = new HttpRequestUtils();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream is = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String startLine = getStartLine(br);
            String requestUrl = getRequestTarget(startLine);
            Map<String, String> httpHeader = getHeader(br);

            String httpBody = ioUtils.readData(br, Integer.parseInt(httpHeader.get(CONTENTE_LENGTH)));
            user.signUp(httpRequestUtils.parseRequestParams(httpBody));

            String rootUrl = WEB_ROOT + requestUrl;

            File rootFile = new File(rootUrl);
            byte[] body = Files.readAllBytes(rootFile.toPath());

            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * http request message에서 StartLine 추출
     * @param br
     * @return Start Line
     * @throws IOException
     */
    private String getStartLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    /**
     *  http request message의 StartLine에서 Request target 추출
     * @param startLine
     * @return Request Target
     */
    private String getRequestTarget(String startLine) {
        return startLine.split(" ")[1];
    }

    /**
     * http request message의 header 추출
     * @param br
     * @return key, value가 분리된 http header
     * @throws IOException
     */
    private Map<String, String> getHeader(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        String line = "";

        while(!(line = br.readLine()).isEmpty()) {
            String[] splitedHeader = line.split(HTTP_HEADER_FIELD_SEPARATOR);
            header.put(splitedHeader[0], splitedHeader[1]);
        }

        return header;
    }

    /**
     * GET요청의 Request Tartget에서 Query String 추출
     * @param requestUrl
     * @return Query String
     */
    private String getQueryStringInHttpHeader(String requestUrl) {
        int queryStringStartIdx = requestUrl.indexOf(QUERY_STRING_PREFIX) + 1;
        StringBuilder sb = new StringBuilder();

        if (queryStringStartIdx != 0) {
            sb.append(requestUrl.substring(queryStringStartIdx));
        }

        return sb.toString();
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
