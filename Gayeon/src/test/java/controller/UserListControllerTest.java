package controller;

import org.junit.Before;
import org.junit.Test;
import webserver.HttpRequest;
import webserver.ResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

public class UserListControllerTest {

    private ResponseHandler responseHandler;
    private HttpRequest httpRequest;
    private UserListController userListContorller;
    private String FILE_NAME = "./webapp/user/list.html";

    @Before
    public void setup() {
        this.responseHandler = mock(ResponseHandler.class);

    }

    @Test
    public void getPageLoginedTest() throws IOException {
        httpRequest = new HttpRequest(new ByteArrayInputStream(createLoginedRequest()));
        httpRequest.parseRequest();
        userListContorller = new UserListController(responseHandler, httpRequest);
        byte[] body = userListContorller.getAllUsers();

        userListContorller.getPage();
        verify(responseHandler).response200Header(body.length);
        verify(responseHandler).responseBody(body);
    }

    @Test
    public void getPageNotLoginedTest() throws IOException {
        httpRequest = new HttpRequest(new ByteArrayInputStream(createNotLoginedRequest()));
        httpRequest.parseRequest();
        userListContorller = new UserListController(responseHandler, httpRequest);
        String loginPage = "/user/login.html";

        userListContorller.getPage();
        verify(responseHandler).response302Header(loginPage);
        verify(responseHandler).responseBody(new byte[0]);
    }

    @Test
    public void addUsers() {

    }

    private byte[] createLoginedRequest() {
        StringBuilder req = new StringBuilder();
        req.append("GET /user/list HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Cookie: logined=true\r\n");
        req.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n");
        req.append("Accept-Encoding: gzip, deflate, br, zstd\r\n");
        req.append("Accept-Language: ko-KR,ko;q=0.9,en-NL;q=0.8,en;q=0.7,en-US;q=0.6\r\n");
        req.append("\r\n");
        return req.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] createNotLoginedRequest() {
        StringBuilder req = new StringBuilder();
        req.append("GET /user/list HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n");
        req.append("Accept-Encoding: gzip, deflate, br, zstd\r\n");
        req.append("Accept-Language: ko-KR,ko;q=0.9,en-NL;q=0.8,en;q=0.7,en-US;q=0.6\r\n");
        req.append("\r\n");
        return req.toString().getBytes(StandardCharsets.UTF_8);
    }
}