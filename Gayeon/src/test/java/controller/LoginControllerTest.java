package controller;

import org.junit.Before;
import org.junit.Test;
import webserver.HttpRequest;
import webserver.ResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoginControllerTest {

    private ResponseHandler responseHandler;
    private HttpRequest httpRequest;
    private LoginController loginController;
    private String FILE_NAME = "./webapp/user/login.html";
    private String loginContent = "userId=testLogin&password=password";

    @Before
    public void setup() {
        this.responseHandler = mock(ResponseHandler.class);

    }

    @Test
    public void getPageTest() throws IOException {
        httpRequest = new HttpRequest(new ByteArrayInputStream(createGetPageRequest()));
        loginController = new LoginController(responseHandler, httpRequest);
        byte[] body = Files.readAllBytes(Paths.get(new File(FILE_NAME).toURI()));

        loginController.getPage();
        verify(responseHandler).response200Header(body.length);
        verify(responseHandler).responseBody(body);
    }

    private byte[] createGetPageRequest() {
        StringBuilder req = new StringBuilder();
        req.append("GET /user/login.html HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n");
        req.append("Accept-Encoding: gzip, deflate, br, zstd\r\n");
        req.append("Accept-Language: ko-KR,ko;q=0.9,en-NL;q=0.8,en;q=0.7,en-US;q=0.6\r\n");
        return req.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] createLoginRequest() {
        int contentLength = loginContent.getBytes(StandardCharsets.UTF_8).length;
        StringBuilder req = new StringBuilder();
        req.append("POST /user/login HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Content-Length: " + contentLength + "\r\n");
        req.append("Content-Type: application/x-www-form-urlencoded");
        req.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n");
        req.append("Accept-Encoding: gzip, deflate, br, zstd\r\n");
        req.append("Accept-Language: ko-KR,ko;q=0.9,en-NL;q=0.8,en;q=0.7,en-US;q=0.6\r\n");
        req.append("\r\n");
        req.append("userId=loginTest&password=password\r\n");
        return req.toString().getBytes(StandardCharsets.UTF_8);
    }
}