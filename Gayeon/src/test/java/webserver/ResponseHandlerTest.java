package webserver;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

public class ResponseHandlerTest {

    private ResponseHandler responseHandler;
    private DataOutputStream dos;
    private Logger log;
    private String testDirectory = "./src/test/resources/";

    @Before
    public void setup() throws FileNotFoundException {
        this.log = mock(Logger.class);
    }

    @Test
    public void response200SuccessTest() throws IOException {
        this.dos = new DataOutputStream(createOutputStream("Http200Response"));
        this.responseHandler = new ResponseHandler(dos, log);
        int lengthOfBodyContent = 0;
        responseHandler.response200Header(lengthOfBodyContent);
    }

    @Test
    public void response302SuccessTest() throws IOException {
        this.dos = new DataOutputStream(createOutputStream("Http302Response"));
        this.responseHandler = new ResponseHandler(dos, log);
        responseHandler.response302Header("index.html");
    }

    @Test
    public void responseLoginSuccessTest() throws IOException {
        this.dos = new DataOutputStream(createOutputStream("HttpLoginSuccessResponse"));
        this.responseHandler = new ResponseHandler(dos, log);
        int lengthOfBodyContent = 0;
        responseHandler.responseLoginSuccessHeader(lengthOfBodyContent);
    }

    @Test
    public void responseCSSSuccessTest() throws IOException {
        this.dos = new DataOutputStream(createOutputStream("HttpCSSResponse"));
        this.responseHandler = new ResponseHandler(dos, log);
        int lengthOfBodyContent = 0;
        responseHandler.responseCSSHeader(lengthOfBodyContent);
    }

    @Test
    public void responseBodyTest() throws IOException {
        this.dos = new DataOutputStream(createOutputStream("HttpResponseBody"));
        this.responseHandler = new ResponseHandler(dos, log);
        responseHandler.responseBody("Hello World".getBytes(StandardCharsets.UTF_8));
    }


    private OutputStream createOutputStream(String file) throws FileNotFoundException {
        return new FileOutputStream(testDirectory + file);
    }
}