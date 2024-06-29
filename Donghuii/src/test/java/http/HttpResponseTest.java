package http;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HttpResponse 테스트")
class HttpResponseTest {

	private String testDirectory = "./src/test/resources/";

	@Test
	@DisplayName("요청 포워드 테스트")
	public void responseForward() throws Exception {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
		response.forward("/index.html");
	}

	@Test
	@DisplayName("요청 리디렉션 테스트")
	public void responseRedirect() throws Exception {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
		response.sendRedirect("/index.html");
	}

	@Test
	@DisplayName("요청 쿠키 테스트")
	public void responseCookies() throws Exception {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Cookies.txt"));
		response.addHeader("Set-Cookie", "logined=true");
		response.sendRedirect("/index.html");
	}

	private OutputStream createOutputStream(String filename) throws Exception {
		return new FileOutputStream(new File(testDirectory + filename));
	}
}
