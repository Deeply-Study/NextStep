package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import controller.CreateUserController;
import controller.LoginUserController;
import http.HttpRequest;
import http.HttpResponse;
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
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "euc-kr"));
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            String ext = "";

            ext = IOUtils.extData(httpRequest.getPath());
            log.debug("ext : {}", ext);

            if ("/user/list.html".equals(httpRequest.getPath())) {
                String auth = httpRequest.getHeader("logined");
                if ("true".equals(auth)) {
                    byte[] data = DataUtils.getUserAll().getBytes();
                    httpResponse.response200Header(data.length, "html");
                    httpResponse.responseBody(data);
                } else {
                    httpResponse.sendRedirect("/user/login.html");
                }
            } else if (ext.equals("html") || ext.equals("css") || ext.equals("js") || ext.equals("ico") || ext.equals("ttf")) {
                httpResponse.responseUrlResource(httpRequest.getPath(), ext);
            } else if ("/user/create".equals(httpRequest.getPath())) { // create user
                new CreateUserController().service(httpRequest, httpResponse);
            } else if ("/user/login".equals(httpRequest.getPath())) { // login
                new LoginUserController().service(httpRequest, httpResponse);
            }

            httpResponse.responseUrlResource("/index.html", HeaderType.HTML.getType());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
