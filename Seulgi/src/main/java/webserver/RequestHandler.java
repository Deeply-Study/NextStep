package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import http.HttpRequest;
import http.HttpResponse;
import http.RequestMapping;
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
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            String ext = httpRequest.getExt();

            Controller controller = RequestMapping.getController(httpRequest.getPath());

            if (controller != null) {
                controller.service(httpRequest, httpResponse);
            } else if (ext.equals("html") || ext.equals("css") || ext.equals("js") || ext.equals("ico") || ext.equals("ttf")) {
                httpResponse.responseUrlResource(httpRequest.getPath(), ext);
            }

            httpResponse.responseUrlResource("/index.html", HeaderType.HTML.getType());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
