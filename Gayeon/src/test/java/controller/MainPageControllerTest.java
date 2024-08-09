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
import static org.mockito.Mockito.*;

public class MainPageControllerTest {

    private ResponseHandler responseHandler;
    private HttpRequest httpRequest;
    private MainPageController mainPageController;
    private String FILE_NAME = "./webapp/index.html";

    @Before
    public void setup() {
        this.responseHandler = mock(ResponseHandler.class);
    }

    @Test
    public void getPageTest() throws IOException {
        httpRequest = new HttpRequest(new ByteArrayInputStream(createGetPageRequest()));
        httpRequest.parseRequest();
        mainPageController = new MainPageController(responseHandler, httpRequest);
        byte[] body = Files.readAllBytes(Paths.get(new File(FILE_NAME).toURI()));

        mainPageController.getPage();
        verify(responseHandler).response200Header(body.length);
        verify(responseHandler).responseBody(body);
    }

    private byte[] createGetPageRequest() {
        StringBuilder req = new StringBuilder();
        req.append("GET /user/index.html HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n");
        req.append("Accept-Encoding: gzip, deflate, br, zstd\r\n");
        req.append("Accept-Language: ko-KR,ko;q=0.9,en-NL;q=0.8,en;q=0.7,en-US;q=0.6\r\n");
        req.append("\r\n");
        return req.toString().getBytes(StandardCharsets.UTF_8);
    }
}