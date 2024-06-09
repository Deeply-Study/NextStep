package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            /**
             * GET / HTTP/1.1
             * Host: localhost:8080
             * Connection: keep-alive
             * sec-ch-ua: "Google Chrome";v="123", "Not:A-Brand";v="8", "Chromium";v="123"
             * sec-ch-ua-mobile: ?0
             * sec-ch-ua-platform: "macOS"
             * Upgrade-Insecure-Requests: 1
             * User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36
             * Sec-Purpose: prefetch;prerender
             * Purpose: prefetch
             * Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng;
             * Cookie:JSESSIONID = 6F ADBCE16B68412F08CD19264E23E66B
             */
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            String readLine;
            String requestLine = buffer.readLine();

            if (requestLine != null && requestLine.startsWith("GET /index.html")) {
                String[] methodUrl = requestLine.split(" ");
                String filePath = methodUrl[1].equals("/index.html") ? "/index.html" : methodUrl[1];

                String indexPath = "/Users/proxy/Next-Step/webapp" + filePath;
                File file = new File(indexPath);
                if (file.exists()) {
                    Path path = file.toPath();
                    byte[] bytes = Files.readAllBytes(path);
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, bytes.length);
                    responseBody(dos, bytes);
                }
            } else if(requestLine != null && requestLine.startsWith("GET /user/create")) {
                String[] methodUrl = requestLine.split(" ");
                System.out.println(Arrays.toString(methodUrl));
                //[GET, /user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net, HTTP/1.1]
                String[] urlSplit = methodUrl[1].split("\\?");
                System.out.println(Arrays.toString(urlSplit));
                //[/user/create, userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net]
                String[] params = urlSplit[1].split("&");
                System.out.println(Arrays.toString(params));
                //[userId=javajigi, password=password, name=JaeSung, email=javajigi%40slipp.net]
                Map<String, String> param = new HashMap<>() {
                    {
                        for(String str : params) {
                            put(str.split("=")[0], str.split("=")[1]);
                        }
                    }
                };//{password=password, name=JaeSung, userId=javajigi, email=javajigi%40slipp.net}
                System.out.println(param);
            } else if(requestLine != null && requestLine.startsWith("POST /user/create")) {
                IOUtils ioUtils = new IOUtils();

            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = "Hello World".getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header( DataOutputStream dos, int lengthOfBodyContent) {
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
