package webserver;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void createHttpGetRequestTest() {
        byte[] byteArr = createGetRequest().getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
        HttpRequest req = new HttpRequest(in);

        assertEquals(req.getReqType(), HttpType.GET);
        assertEquals(req.getReqPath(), "/index.html");
    }

    @Test
    public void createHttpPostRequestTest() {
        byte[] byteArr = createPostRequest().getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
        HttpRequest req = new HttpRequest(in);

        assertEquals(req.getReqType(), HttpType.POST);
        assertEquals(req.getReqPath(), "/user/create");
    }

    @Test
    public void parseGetRequestTest() throws IOException {
        byte[] byteArr = createGetRequest().getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
        HttpRequest req = new HttpRequest(in);
        req.parseRequest();
        Map<String, String> expected = new HashMap<>();
        expected.put("Host", "localhost:8080");
        expected.put("Connection", "keep-alive");
        expected.put("Accept", "*/*");
        expected.put("Cookie", "logined=true");
        assertEquals(expected, req.getHeaders());
    }

    @Test
    public void parsePostRequestTest() throws IOException {
        byte[] byteArr = createPostRequest().getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
        HttpRequest req = new HttpRequest(in);
        req.parseRequest();

        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Host", "localhost:8080");
        expectedHeaders.put("Connection", "keep-alive");
        expectedHeaders.put("Content-Length", "66");
        expectedHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        expectedHeaders.put("Accept", "*/*");
        assertEquals(expectedHeaders, req.getHeaders());

        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("userId","test");
        expectedParams.put("password", "password");
        expectedParams.put("name", "Mina");
        expectedParams.put("email", "minatest@gmail.com");
        assertEquals(expectedParams, req.getParams());
    }

    private String createGetRequest() {
        StringBuilder req = new StringBuilder();
        req.append("GET /index.html HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Accept: */*\r\n");
        req.append("Cookie: logined=true\r\n");
        req.append("\r\n");
        return req.toString();
    }

    private String createPostRequest() {
        StringBuilder req = new StringBuilder();
        String content = "userId=test&password=password&name=Mina&email=minatest@gmail.com\r\n";
        int contentLength = content.getBytes(StandardCharsets.UTF_8).length;
        req.append("POST /user/create HTTP/1.1\r\n");
        req.append("Host: localhost:8080\r\n");
        req.append("Connection: keep-alive\r\n");
        req.append("Content-Length: " + contentLength + "\r\n");
        req.append("Content-Type: application/x-www-form-urlencoded\r\n");
        req.append("Accept: */*\r\n");
        req.append("\r\n");
        req.append(content);
        return req.toString();
    }
}