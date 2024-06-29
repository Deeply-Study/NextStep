package http;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HttpRequest 테스트")
class HttpRequestTest {

	private final String testDirectory = "./src/test/resources/";

	@Test
	@DisplayName("GET 요청 테스트")
	public void requestGet() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
		HttpRequest request = new HttpRequest(in);

		assertEquals(HttpMethod.GET, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("javajigi", request.getparameter("userId"));
	}

	@Test
	@DisplayName("POST 요청 테스트")
	public void requestPost() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
		HttpRequest request = new HttpRequest(in);

		assertEquals(HttpMethod.POST, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("javajigi", request.getparameter("userId"));
	}

	@Test
	@DisplayName("POST 요청 테스트 222")
	public void requestPost2() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST2.txt"));
		HttpRequest request = new HttpRequest(in);

		assertEquals(HttpMethod.POST, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("1", request.getparameter("id"));
		assertEquals("javajigi", request.getparameter("userId"));
	}
}
