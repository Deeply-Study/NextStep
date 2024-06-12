package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DataUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            String line = bf.readLine();

            if (!"".equals(line) && line != null) {
                // HTML 페이지 나타내기
                if (line.matches(".*.html.*")) {
                    DataOutputStream dos = new DataOutputStream(out);
                    String url = IOUtils.urlData(line);
                    log.debug("url : {}", url);

                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                // create user
                } else if (line.matches(".*/user/create.*")) {
                    String contentsLength = "0";

                    while (true) {
                        String read = bf.readLine();
                        if (read != null && read.contains("Content-Length")) {
                            contentsLength = IOUtils.bodyData(read);
                            log.debug("contents-length : {}", contentsLength);
                        }

                        if (read.length() == 0) {
                            String request = IOUtils.readData(bf, Integer.parseInt(contentsLength));
                            log.debug("request : {}", request);

                            DataUtils.createUser(request);
                            break;
                        }
                    }
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello World".getBytes();
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
