package webserver;

import static util.Constants.SIGN_UP_PATH;
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
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private model.User user = new User();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream is = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String httpRequestFirstLine = br.readLine();
            String requestUrl = httpRequestFirstLine.split(" ")[1];

            int queryStringStartIdx = requestUrl.indexOf("?") + 1;
            String queryString = "";
            if (queryStringStartIdx != 0) {
                 queryString = requestUrl.substring(queryStringStartIdx);
            }

            if (requestUrl.startsWith(SIGN_UP_PATH)) {
                user = user.signUp(queryString);
            }

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
